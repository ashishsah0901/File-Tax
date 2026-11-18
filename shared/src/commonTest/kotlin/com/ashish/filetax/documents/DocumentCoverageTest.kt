package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.DocumentPlacements
import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.logic.DocumentTrust
import com.ashish.filetax.documents.logic.ItrDetailMap
import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DocumentCoverageTest {

    @Test
    fun everyDocumentKnowsWhereItsContentsGo() {
        DocumentKind.entries.forEach { kind ->
            assertTrue(DocumentPlacements.forKind(kind).isNotBlank(), "${kind.name} has no placement")
        }
    }

    @Test
    fun everyDocumentHasAPlaceInTheOrderOfTrust() {
        val ranked = DocumentTrust.ranked()
        assertEquals(DocumentKind.entries.size, ranked.size)
        assertEquals(DocumentKind.entries.toSet(), ranked.toSet())
    }

    @Test
    fun everyDetailKnowsWhereItGoes() {
        DetailKind.entries.forEach { kind ->
            val field = ItrDetailMap.forKind(kind)
            assertTrue(field.schedule in ItrSchedule.entries, "${kind.name} has an unknown schedule")
            assertTrue(field.field.isNotBlank(), "${kind.name} has no field")
            assertTrue(field.note.isNotBlank(), "${kind.name} has no note")
        }
    }

    @Test
    fun everyDocumentAnyoneCanBeAskedForCanBeRead() {
        val everything = OnboardingAnswers(
            residentialStatus = ResidentialStatus.RESIDENT,
            incomeSources = IncomeSource.entries.toSet(),
            capitalGainKinds = CapitalGainKind.entries.toSet(),
            housePropertyCount = HousePropertyCount.MORE_THAN_TWO,
            businessKinds = BusinessKind.entries.toSet(),
            agriculturalIncomeAboveLimit = true,
            totalIncomeBand = TotalIncomeBand.ABOVE_ONE_CRORE,
            specialSituations = SpecialSituation.entries.toSet()
        )
        val asked = DocumentChecklist.forAnswers(everything).map { it.id }.toSet()
        val readable = DocumentKind.entries.mapNotNull { it.checklistId }.toSet()

        assertTrue((asked - readable).isEmpty(), "cannot be read: ${asked - readable}")
    }

    @Test
    fun everyDocumentThatIsOnTheChecklistCanBeRecognised() {
        val samples = mapOf(
            DocumentKind.PAN_CARD to DocumentSamples.panCard,
            DocumentKind.AADHAAR to DocumentSamples.aadhaar,
            DocumentKind.BANK_ACCOUNT_PROOF to DocumentSamples.bankAccountProof,
            DocumentKind.TAX_PAYMENT_CHALLAN to DocumentSamples.taxPaymentChallan,
            DocumentKind.TAX_AUDIT_REPORT to DocumentSamples.taxAuditReport,
            DocumentKind.FORM_10IEA to DocumentSamples.form10iea,
            DocumentKind.FORM_16 to DocumentSamples.form16,
            DocumentKind.FORM_12BA to DocumentSamples.form12ba,
            DocumentKind.FULL_AND_FINAL_SETTLEMENT to DocumentSamples.fullAndFinalSettlement,
            DocumentKind.FORM_10E to DocumentSamples.form10e,
            DocumentKind.ESOP_PERQUISITE_STATEMENT to DocumentSamples.esopStatement,
            DocumentKind.FORM_16B_OR_16C to DocumentSamples.form16b,
            DocumentKind.INTEREST_CERTIFICATE to DocumentSamples.bankInterestCertificate,
            DocumentKind.RENT_RECORDS to DocumentSamples.rentRecords,
            DocumentKind.PROPERTY_SALE_PAPERS to DocumentSamples.propertySale,
            DocumentKind.CAPITAL_GAINS_STATEMENT to DocumentSamples.capitalGains,
            DocumentKind.MUTUAL_FUND_STATEMENT to DocumentSamples.mutualFundStatement,
            DocumentKind.CRYPTO_STATEMENT to DocumentSamples.cryptoStatement,
            DocumentKind.PROFIT_AND_LOSS to DocumentSamples.profitAndLoss,
            DocumentKind.BALANCE_SHEET to DocumentSamples.balanceSheet,
            DocumentKind.TURNOVER_SUMMARY to DocumentSamples.turnoverSummary,
            DocumentKind.GST_RETURN to DocumentSamples.gstReturn,
            DocumentKind.FIRM_SHARE_STATEMENT to DocumentSamples.firmShare,
            DocumentKind.WINNINGS_CERTIFICATE to DocumentSamples.winningsCertificate,
            DocumentKind.AGRICULTURAL_RECORDS to DocumentSamples.agriculturalRecords,
            DocumentKind.FOREIGN_TAX_PROOF to DocumentSamples.foreignTaxProof,
            DocumentKind.DEDUCTION_PROOFS to DocumentSamples.deductionProofs,
            DocumentKind.LAST_YEAR_RETURN to DocumentSamples.lastYearReturn,
            DocumentKind.FORM_26AS to DocumentSamples.form26as,
            DocumentKind.ANNUAL_INFORMATION_STATEMENT to DocumentSamples.ais
        )

        samples.forEach { (kind, sample) ->
            assertEquals(kind, DocumentScanner.parse("${kind.name}.pdf", sample).kind)
        }
    }
}