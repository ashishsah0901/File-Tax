package com.ashish.filetax.documents.model

data class ExtractedFigure(
    val kind: FigureKind,
    val amount: Long,
    val sourceLine: String
)