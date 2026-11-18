package com.ashish.filetax.documents.model

import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.onboarding.model.TaxDocument

enum class CrossCheckStatus {
    MATCH,
    MISMATCH,
    INFORMATION
}

data class CrossCheck(
    val title: String,
    val status: CrossCheckStatus,
    val detail: String
)

data class ReportSection(
    val schedule: ItrSchedule,
    val entries: List<ReportEntry>,
    val details: List<DetailEntry>
)

data class ScanReport(
    val documents: List<ParsedDocument>,
    val sections: List<ReportSection>,
    val checks: List<CrossCheck>,
    val stillToScan: List<TaxDocument>
)