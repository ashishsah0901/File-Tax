package com.ashish.filetax.documents.data

sealed interface PdfExtraction {

    data class Text(val text: String) : PdfExtraction

    data object PasswordRequired : PdfExtraction

    data object WrongPassword : PdfExtraction

    data class Failed(val reason: String) : PdfExtraction
}

interface PdfTextExtractor {
    suspend fun extract(bytes: ByteArray, password: String?): PdfExtraction
}

expect fun pdfTextExtractor(): PdfTextExtractor