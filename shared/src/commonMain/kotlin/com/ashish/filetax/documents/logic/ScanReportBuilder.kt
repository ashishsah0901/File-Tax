package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailEntry
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.documents.model.ReportEntry
import com.ashish.filetax.documents.model.ReportSection
import com.ashish.filetax.documents.model.ScanReport
import com.ashish.filetax.onboarding.model.TaxDocument

object ScanReportBuilder {

    fun build(documents: List<ParsedDocument>, checklist: List<TaxDocument>): ScanReport {
        val entries = documents
            .sortedBy { DocumentTrust.rankOf(it.kind) }
            .flatMap { document -> document.figures.map { document.kind to it } }
            .distinctBy { (_, figure) -> figure.kind }
            .map { (kind, figure) ->
                ReportEntry(figure = figure, field = ItrFieldMap.forKind(figure.kind), source = kind)
            }

        val details = documents
            .sortedBy { DocumentTrust.rankOf(it.kind) }
            .flatMap { document -> document.details.map { document.kind to it } }
            .distinctBy { (_, detail) -> detail.kind to detail.value }
            .map { (kind, detail) ->
                DetailEntry(detail = detail, field = ItrDetailMap.forKind(detail.kind), source = kind)
            }

        val sections = ItrSchedule.entries.mapNotNull { schedule ->
            val figuresInSchedule = entries.filter { it.field.schedule == schedule }
            val detailsInSchedule = details.filter { it.field.schedule == schedule }
            if (figuresInSchedule.isEmpty() && detailsInSchedule.isEmpty()) {
                null
            } else {
                ReportSection(schedule = schedule, entries = figuresInSchedule, details = detailsInSchedule)
            }
        }

        val scannable = DocumentKind.entries.mapNotNull { it.checklistId }.toSet()
        val scanned = documents.mapNotNull { it.kind.checklistId }.toSet()

        return ScanReport(
            documents = documents,
            sections = sections,
            checks = CrossChecks.forDocuments(documents),
            stillToScan = checklist.filter { it.id in scannable && it.id !in scanned }
        )
    }
}