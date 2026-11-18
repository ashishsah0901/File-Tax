package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.logic.ScanReportBuilder
import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FilingDetailsScannerTest {

    @Test
    fun aPanCardGivesUpThePan() {
        val parsed = DocumentScanner.parse("pan.pdf", DocumentSamples.panCard)

        assertEquals(DocumentKind.PAN_CARD, parsed.kind)
        assertEquals("ABCDE1234F", parsed.valueOf(DetailKind.PAN))
    }

    @Test
    fun anAadhaarGivesUpTheNumberUsedToVerifyTheReturn() {
        val parsed = DocumentScanner.parse("aadhaar.pdf", DocumentSamples.aadhaar)

        assertEquals(DocumentKind.AADHAAR, parsed.kind)
        assertEquals("2345 6789 0123", parsed.valueOf(DetailKind.AADHAAR_NUMBER))
    }

    @Test
    fun aCancelledChequeGivesUpTheRefundAccount() {
        val parsed = DocumentScanner.parse("cheque.pdf", DocumentSamples.bankAccountProof)

        assertEquals(DocumentKind.BANK_ACCOUNT_PROOF, parsed.kind)
        assertEquals("12345678901", parsed.valueOf(DetailKind.BANK_ACCOUNT_NUMBER))
        assertEquals("SBIN0001234", parsed.valueOf(DetailKind.IFSC))
        assertEquals("State Bank of India", parsed.valueOf(DetailKind.BANK_NAME))
    }

    @Test
    fun aChallanGivesUpEverythingScheduleItAsksFor() {
        val parsed = DocumentScanner.parse("challan.pdf", DocumentSamples.taxPaymentChallan)

        assertEquals(DocumentKind.TAX_PAYMENT_CHALLAN, parsed.kind)
        assertEquals(45000L, parsed.amountOf(FigureKind.SELF_ASSESSMENT_TAX))
        assertEquals("0510308", parsed.valueOf(DetailKind.CHALLAN_BSR_CODE))
        assertEquals("54321", parsed.valueOf(DetailKind.CHALLAN_SERIAL_NUMBER))
        assertEquals("28/06/2026", parsed.valueOf(DetailKind.CHALLAN_DATE))
    }

    @Test
    fun anAuditReportGivesUpTheUdinAndTheTurnover() {
        val parsed = DocumentScanner.parse("3cd.pdf", DocumentSamples.taxAuditReport)

        assertEquals(DocumentKind.TAX_AUDIT_REPORT, parsed.kind)
        assertEquals("21123456AAAABB1234", parsed.valueOf(DetailKind.AUDIT_REPORT_UDIN))
        assertEquals(8450000L, parsed.amountOf(FigureKind.BUSINESS_RECEIPTS))
        assertEquals(1370000L, parsed.amountOf(FigureKind.BUSINESS_NET_PROFIT))
    }

    @Test
    fun form10ieaGivesUpTheAcknowledgementTheReturnAsksFor() {
        val parsed = DocumentScanner.parse("10iea.pdf", DocumentSamples.form10iea)

        assertEquals(DocumentKind.FORM_10IEA, parsed.kind)
        assertEquals("123456789012345", parsed.valueOf(DetailKind.ACKNOWLEDGEMENT_NUMBER))
    }

    @Test
    fun aFormSixteenGivesUpTheEmployersTanToClaimTdsAgainst() {
        val parsed = DocumentScanner.parse("form16.pdf", DocumentSamples.form16)

        assertEquals("BLRA12345B", parsed.valueOf(DetailKind.DEDUCTOR_TAN))
        assertEquals("ABCDE1234F", parsed.valueOf(DetailKind.PAN))
    }

    @Test
    fun theseDetailsAreReportedUnderTheRightPartOfTheReturn() {
        val report = ScanReportBuilder.build(
            documents = listOf(
                DocumentScanner.parse("pan.pdf", DocumentSamples.panCard),
                DocumentScanner.parse("aadhaar.pdf", DocumentSamples.aadhaar),
                DocumentScanner.parse("cheque.pdf", DocumentSamples.bankAccountProof),
                DocumentScanner.parse("challan.pdf", DocumentSamples.taxPaymentChallan)
            ),
            checklist = DocumentChecklist.forAnswers(
                OnboardingAnswers(
                    residentialStatus = ResidentialStatus.RESIDENT,
                    incomeSources = setOf(IncomeSource.SALARY_PENSION)
                )
            )
        )

        val filingDetails = report.sections.single { it.schedule == ItrSchedule.FILING_DETAILS }
        assertEquals(
            listOf(
                DetailKind.PAN,
                DetailKind.AADHAAR_NUMBER,
                DetailKind.BANK_ACCOUNT_NUMBER,
                DetailKind.IFSC,
                DetailKind.BANK_NAME
            ),
            filingDetails.details.map { it.detail.kind }
        )

        val taxesPaid = report.sections.single { it.schedule == ItrSchedule.TAXES_PAID }
        assertTrue(taxesPaid.details.any { it.detail.kind == DetailKind.CHALLAN_BSR_CODE })
        assertTrue(taxesPaid.entries.any { it.figure.kind == FigureKind.SELF_ASSESSMENT_TAX })
    }

    @Test
    fun whatIsStillMissingNowCoversThePanAadhaarAndBankAccount() {
        val report = ScanReportBuilder.build(
            documents = listOf(DocumentScanner.parse("form16.pdf", DocumentSamples.form16)),
            checklist = DocumentChecklist.forAnswers(
                OnboardingAnswers(
                    residentialStatus = ResidentialStatus.RESIDENT,
                    incomeSources = setOf(IncomeSource.SALARY_PENSION)
                )
            )
        )

        assertTrue(report.stillToScan.any { it.id == "pan" })
        assertTrue(report.stillToScan.any { it.id == "aadhaar" })
        assertTrue(report.stillToScan.any { it.id == "bank_account" })
    }
}