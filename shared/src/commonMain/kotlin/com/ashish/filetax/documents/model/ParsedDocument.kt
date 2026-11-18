package com.ashish.filetax.documents.model

data class DocumentIdentity(
    val pan: String? = null,
    val tan: String? = null,
    val assessmentYear: String? = null,
    val issuer: String? = null
)

data class ParsedDocument(
    val fileName: String,
    val kind: DocumentKind,
    val identity: DocumentIdentity,
    val figures: List<ExtractedFigure>,
    val details: List<ExtractedDetail> = emptyList()
) {
    fun amountOf(kind: FigureKind): Long? = figures.firstOrNull { it.kind == kind }?.amount

    fun valueOf(kind: DetailKind): String? = details.firstOrNull { it.kind == kind }?.value
}