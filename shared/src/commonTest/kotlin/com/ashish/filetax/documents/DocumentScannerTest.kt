package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DocumentScannerTest {

    @Test
    fun form16IsRecognisedAndRead() {
        val parsed = DocumentScanner.parse("form16.pdf", DocumentSamples.form16)

        assertEquals(DocumentKind.FORM_16, parsed.kind)
        assertEquals("ABCDE1234F", parsed.identity.pan)
        assertEquals("BLRA12345B", parsed.identity.tan)
        assertEquals("2026-27", parsed.identity.assessmentYear)
        assertEquals(1845000L, parsed.amountOf(FigureKind.GROSS_SALARY))
        assertEquals(62000L, parsed.amountOf(FigureKind.PERQUISITES))
        assertEquals(240000L, parsed.amountOf(FigureKind.EXEMPT_ALLOWANCES))
        assertEquals(75000L, parsed.amountOf(FigureKind.STANDARD_DEDUCTION))
        assertEquals(2400L, parsed.amountOf(FigureKind.PROFESSIONAL_TAX))
        assertEquals(1589600L, parsed.amountOf(FigureKind.INCOME_CHARGEABLE_UNDER_SALARY))
        assertEquals(18000L, parsed.amountOf(FigureKind.OTHER_INCOME_REPORTED_TO_EMPLOYER))
        assertEquals(1607600L, parsed.amountOf(FigureKind.GROSS_TOTAL_INCOME))
        assertEquals(150000L, parsed.amountOf(FigureKind.DEDUCTION_80C))
        assertEquals(50000L, parsed.amountOf(FigureKind.DEDUCTION_80CCD1B))
        assertEquals(25000L, parsed.amountOf(FigureKind.DEDUCTION_80D))
        assertEquals(225000L, parsed.amountOf(FigureKind.TOTAL_CHAPTER_VIA_DEDUCTIONS))
        assertEquals(1382600L, parsed.amountOf(FigureKind.TOTAL_TAXABLE_INCOME))
        assertEquals(219210L, parsed.amountOf(FigureKind.TDS_ON_SALARY))
    }

    @Test
    fun aisIsRecognisedAndRead() {
        val parsed = DocumentScanner.parse("ais.pdf", DocumentSamples.ais)

        assertEquals(DocumentKind.ANNUAL_INFORMATION_STATEMENT, parsed.kind)
        assertEquals("ABCDE1234F", parsed.identity.pan)
        assertEquals(1907000L, parsed.amountOf(FigureKind.GROSS_SALARY))
        assertEquals(18500L, parsed.amountOf(FigureKind.SAVINGS_INTEREST))
        assertEquals(64200L, parsed.amountOf(FigureKind.DEPOSIT_INTEREST))
        assertEquals(12800L, parsed.amountOf(FigureKind.DIVIDEND))
        assertEquals(480000L, parsed.amountOf(FigureKind.SALE_OF_SECURITIES))
        assertEquals(3500000L, parsed.amountOf(FigureKind.PURCHASE_OF_IMMOVABLE_PROPERTY))
        assertEquals(250000L, parsed.amountOf(FigureKind.FOREIGN_REMITTANCE))
        assertEquals(21400L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
        assertNull(parsed.amountOf(FigureKind.TCS))
    }

    @Test
    fun form26asTotalsTdsPerSection() {
        val parsed = DocumentScanner.parse("26as.pdf", DocumentSamples.form26as)

        assertEquals(DocumentKind.FORM_26AS, parsed.kind)
        assertEquals(219210L, parsed.amountOf(FigureKind.TDS_ON_SALARY))
        assertEquals(6420L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
        assertEquals(25000L, parsed.amountOf(FigureKind.ADVANCE_TAX))
        assertEquals(12000L, parsed.amountOf(FigureKind.SELF_ASSESSMENT_TAX))
    }

    @Test
    fun capitalGainsStatementIsRead() {
        val parsed = DocumentScanner.parse("gains.pdf", DocumentSamples.capitalGains)

        assertEquals(DocumentKind.CAPITAL_GAINS_STATEMENT, parsed.kind)
        assertEquals(48000L, parsed.amountOf(FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY))
        assertEquals(172000L, parsed.amountOf(FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY))
        assertEquals(480000L, parsed.amountOf(FigureKind.SALE_OF_SECURITIES))
    }

    @Test
    fun interestCertificateIsRead() {
        val parsed = DocumentScanner.parse("interest.pdf", DocumentSamples.bankInterestCertificate)

        assertEquals(DocumentKind.INTEREST_CERTIFICATE, parsed.kind)
        assertEquals(18500L, parsed.amountOf(FigureKind.SAVINGS_INTEREST))
        assertEquals(64200L, parsed.amountOf(FigureKind.DEPOSIT_INTEREST))
        assertEquals(6420L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun anythingElseIsReportedAsUnrecognised() {
        val parsed = DocumentScanner.parse("menu.pdf", "Lunch menu\nPaneer butter masala 320.00")

        assertEquals(DocumentKind.UNRECOGNISED, parsed.kind)
        assertTrue(parsed.figures.isEmpty())
    }
}