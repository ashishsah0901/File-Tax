package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object Form26asParser {

    private val TAN = Regex("[A-Z]{4}[0-9]{5}[A-Z]")
    private val SALARY_SECTION = Regex("\\b192[A-B]?\\b")
    private val OTHER_TDS_SECTION = Regex("\\b19[3-5][A-Z]{0,2}\\b")
    private val TCS_SECTION = Regex("\\b206C[A-Z]{0,2}\\b")

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val deductorRows = text.lines.filter { TAN.containsMatchIn(it) }
        val figures = buildList<ExtractedFigure> {
            addRows(deductorRows, FigureKind.TDS_ON_SALARY, SALARY_SECTION)
            addRows(deductorRows, FigureKind.TDS_OTHER_THAN_SALARY, OTHER_TDS_SECTION)
            addRows(deductorRows, FigureKind.TCS, TCS_SECTION)
            add(text, FigureKind.ADVANCE_TAX, "advance tax")
            add(text, FigureKind.SELF_ASSESSMENT_TAX, "self assessment tax")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_26AS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }

    private fun MutableList<ExtractedFigure>.addRows(rows: List<String>, kind: FigureKind, section: Regex) {
        val matching = rows.filter { section.containsMatchIn(it) }
        if (matching.isEmpty()) return
        val total = matching.sumOf { Amounts.lastIn(it) ?: 0L }
        if (total == 0L) return
        add(
            ExtractedFigure(
                kind = kind,
                amount = total,
                sourceLine = if (matching.size > 1) {
                    "${matching.size} deductor rows, starting ${matching.first()}"
                } else {
                    matching.first()
                }
            )
        )
    }
}