package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind

internal fun MutableList<ExtractedFigure>.add(
    text: DocumentText,
    kind: FigureKind,
    vararg needles: String,
    skip: List<String> = emptyList()
) {
    if (any { it.kind == kind }) return
    val amount = text.amountFor(*needles, skip = skip) ?: return
    add(ExtractedFigure(kind = kind, amount = amount.value, sourceLine = amount.sourceLine))
}

internal fun MutableList<ExtractedFigure>.addSum(
    text: DocumentText,
    kind: FigureKind,
    vararg needles: String
) {
    if (any { it.kind == kind }) return
    val amount = text.sumOf(*needles) ?: return
    add(ExtractedFigure(kind = kind, amount = amount.value, sourceLine = amount.sourceLine))
}