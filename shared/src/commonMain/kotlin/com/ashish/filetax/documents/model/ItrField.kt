package com.ashish.filetax.documents.model

import com.ashish.filetax.itr.ItrSchedule

data class ItrField(
    val schedule: ItrSchedule,
    val field: String,
    val note: String
)

data class ReportEntry(
    val figure: ExtractedFigure,
    val field: ItrField,
    val source: DocumentKind
)

data class DetailEntry(
    val detail: ExtractedDetail,
    val field: ItrField,
    val source: DocumentKind
)