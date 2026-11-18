package com.ashish.filetax.documents.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.create
import platform.PDFKit.PDFDocument

private class PdfKitTextExtractor : PdfTextExtractor {

    override suspend fun extract(bytes: ByteArray, password: String?): PdfExtraction =
        withContext(Dispatchers.Default) {
            val data = bytes.toNSData() ?: return@withContext PdfExtraction.Failed("The file is empty.")
            val document = runCatching { PDFDocument(data) }.getOrNull()
                ?: return@withContext PdfExtraction.Failed("This file could not be read as a PDF.")
            if (document.isLocked()) {
                if (password.isNullOrEmpty()) return@withContext PdfExtraction.PasswordRequired
                if (!document.unlockWithPassword(password)) return@withContext PdfExtraction.WrongPassword
            }
            val text = buildString {
                for (index in 0uL until document.pageCount) {
                    val page = document.pageAtIndex(index) ?: continue
                    page.string?.let {
                        append(it)
                        append('\n')
                    }
                }
            }
            if (text.isBlank()) {
                PdfExtraction.Failed("No text could be read from this PDF. A scanned image cannot be read.")
            } else {
                PdfExtraction.Text(text)
            }
        }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData? {
    if (isEmpty()) return null
    return usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = size.toULong())
    }
}

actual fun pdfTextExtractor(): PdfTextExtractor = PdfKitTextExtractor()