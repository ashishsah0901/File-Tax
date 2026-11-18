package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.CrossChecks
import com.ashish.filetax.documents.model.CrossCheckStatus
import com.ashish.filetax.documents.model.DocumentIdentity
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrossChecksTest {

    @Test
    fun salaryAndTdsThatAgreeAreReportedAsMatches() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(DocumentKind.FORM_16, FigureKind.GROSS_SALARY to 1907000L, FigureKind.TDS_ON_SALARY to 219210L),
                document(DocumentKind.ANNUAL_INFORMATION_STATEMENT, FigureKind.GROSS_SALARY to 1907000L),
                document(DocumentKind.FORM_26AS, FigureKind.TDS_ON_SALARY to 219210L)
            )
        )

        assertEquals(2, checks.size)
        assertTrue(checks.all { it.status == CrossCheckStatus.MATCH })
    }

    @Test
    fun tdsShortOfForm26asIsReportedAsMismatch() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(DocumentKind.FORM_16, FigureKind.TDS_ON_SALARY to 219210L),
                document(DocumentKind.FORM_26AS, FigureKind.TDS_ON_SALARY to 180000L)
            )
        )

        val check = checks.single()
        assertEquals(CrossCheckStatus.MISMATCH, check.status)
        assertTrue(check.detail.contains("₹39,210"))
    }

    @Test
    fun aSaleOfSecuritiesWithoutABrokerStatementIsCalledOut() {
        val checks = CrossChecks.forDocuments(
            listOf(document(DocumentKind.ANNUAL_INFORMATION_STATEMENT, FigureKind.SALE_OF_SECURITIES to 480000L))
        )

        val check = checks.single()
        assertEquals(CrossCheckStatus.INFORMATION, check.status)
        assertTrue(check.detail.contains("broker"))
    }

    @Test
    fun documentsBelongingToDifferentPansAreCalledOut() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(DocumentKind.FORM_16, pan = "ABCDE1234F"),
                document(DocumentKind.FORM_26AS, pan = "ZZZZZ9999Z")
            )
        )

        val check = checks.single()
        assertEquals(CrossCheckStatus.MISMATCH, check.status)
        assertTrue(check.detail.contains("ZZZZZ9999Z"))
    }

    @Test
    fun aDocumentFromAnotherYearIsCalledOut() {
        val checks = CrossChecks.forDocuments(
            listOf(document(DocumentKind.FORM_16, assessmentYear = "2021-22"))
        )

        assertEquals(CrossCheckStatus.INFORMATION, checks.single().status)
    }

    @Test
    fun aLossOnVirtualDigitalAssetsIsCalledOut() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(
                    DocumentKind.CRYPTO_STATEMENT,
                    FigureKind.VDA_SALE_CONSIDERATION to 540000L,
                    FigureKind.VDA_COST_OF_ACQUISITION to 620000L
                )
            )
        )

        val check = checks.single()
        assertEquals(CrossCheckStatus.INFORMATION, check.status)
        assertTrue(check.detail.contains("₹80,000"))
    }

    @Test
    fun rentInAisWithoutRentRecordsIsCalledOut() {
        val checks = CrossChecks.forDocuments(
            listOf(document(DocumentKind.ANNUAL_INFORMATION_STATEMENT, FigureKind.RENT_RECEIVED to 480000L))
        )

        val check = checks.single()
        assertEquals(CrossCheckStatus.INFORMATION, check.status)
        assertTrue(check.title.contains("₹4,80,000"))
    }

    @Test
    fun turnoverInTheBooksIsComparedWithTheGstReturn() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(DocumentKind.PROFIT_AND_LOSS, FigureKind.BUSINESS_RECEIPTS to 8450000L),
                document(DocumentKind.GST_RETURN, FigureKind.GST_TURNOVER to 4500000L)
            )
        )

        val check = checks.single { it.title.contains("GST") }
        assertEquals(CrossCheckStatus.MISMATCH, check.status)
    }

    @Test
    fun aTenantsPanOnARentReceiptIsNotMistakenForYours() {
        val checks = CrossChecks.forDocuments(
            listOf(
                document(DocumentKind.FORM_16, pan = "ABCDE1234F"),
                document(DocumentKind.RENT_RECORDS, pan = "ZZZZZ9999Z")
            )
        )

        assertTrue(checks.isEmpty())
    }

    private fun document(
        kind: DocumentKind,
        vararg figures: Pair<FigureKind, Long>,
        pan: String? = null,
        assessmentYear: String? = null
    ) = ParsedDocument(
        fileName = "${kind.name.lowercase()}.pdf",
        kind = kind,
        identity = DocumentIdentity(pan = pan, assessmentYear = assessmentYear),
        figures = figures.map { (figureKind, amount) ->
            ExtractedFigure(kind = figureKind, amount = amount, sourceLine = figureKind.label)
        }
    )
}