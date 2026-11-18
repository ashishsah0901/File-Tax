package com.ashish.filetax.documents.data

import android.content.Context
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.encryption.InvalidPasswordException
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object AndroidPdfSupport {

    fun install(context: Context) {
        PDFBoxResourceLoader.init(context.applicationContext)
    }
}

private class PdfBoxTextExtractor : PdfTextExtractor {

    override suspend fun extract(bytes: ByteArray, password: String?): PdfExtraction =
        withContext(Dispatchers.IO) {
            try {
                PDDocument.load(bytes, password.orEmpty()).use { document ->
                    PdfExtraction.Text(PDFTextStripper().getText(document))
                }
            } catch (error: InvalidPasswordException) {
                if (password.isNullOrEmpty()) PdfExtraction.PasswordRequired else PdfExtraction.WrongPassword
            } catch (error: IOException) {
                PdfExtraction.Failed(error.message ?: "This file could not be read as a PDF.")
            }
        }
}

actual fun pdfTextExtractor(): PdfTextExtractor = PdfBoxTextExtractor()