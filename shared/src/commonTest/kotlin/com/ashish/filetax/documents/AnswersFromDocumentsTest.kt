package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.AnswersFromDocuments
import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.onboarding.logic.ItrFormDeterminer
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AnswersFromDocumentsTest {

    private fun parse(vararg samples: Pair<String, String>) =
        samples.map { (name, text) -> DocumentScanner.parse(name, text) }

    @Test
    fun aSalariedFolderFillsInWhatItCan() {
        val inferred = AnswersFromDocuments.infer(
            parse(
                "form16.pdf" to DocumentSamples.form16,
                "interest.pdf" to DocumentSamples.bankInterestCertificate
            )
        )

        assertEquals(
            setOf(IncomeSource.SALARY_PENSION, IncomeSource.INTEREST_DIVIDEND_OTHER),
            inferred.answers.incomeSources
        )
        assertEquals(TotalIncomeBand.UP_TO_FIFTY_LAKH, inferred.answers.totalIncomeBand)
        assertTrue(inferred.findings.any { it.label == "Salary or pension" })
        assertTrue(inferred.findings.any { it.evidence.contains("form16.pdf") })
    }

    @Test
    fun itNeverPretendsToKnowWhatNoPdfCanSay() {
        val inferred = AnswersFromDocuments.infer(parse("form16.pdf" to DocumentSamples.form16))

        assertNull(inferred.answers.residentialStatus)
        assertTrue(inferred.stillToAnswer.any { it.contains("live in India") })
    }

    @Test
    fun aisPullsInTheSaleOfSharesTheTaxpayerForgot() {
        val inferred = AnswersFromDocuments.infer(
            parse(
                "form16.pdf" to DocumentSamples.form16,
                "ais.pdf" to DocumentSamples.ais
            )
        )

        assertTrue(IncomeSource.CAPITAL_GAINS in inferred.answers.incomeSources.orEmpty())
        assertTrue(CapitalGainKind.LISTED_EQUITY_LONG_TERM in inferred.answers.capitalGainKinds.orEmpty())
    }

    @Test
    fun aSmallLongTermGainIsRecognisedAsStillInsideTheLimit() {
        val inferred = AnswersFromDocuments.infer(
            parse("gains.pdf" to "Capital Gains Statement FY 2025-26\nTotal Long Term Capital Gain 96000.00")
        )

        assertEquals(setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM), inferred.answers.capitalGainKinds)
        assertEquals(true, inferred.answers.equityLongTermGainWithinExemptLimit)
    }

    @Test
    fun booksAndAFirmStatementMakeItABusinessReturn() {
        val inferred = AnswersFromDocuments.infer(
            parse(
                "pnl.pdf" to DocumentSamples.profitAndLoss,
                "firm.pdf" to DocumentSamples.firmShare,
                "gst.pdf" to DocumentSamples.gstReturn
            )
        )

        assertTrue(IncomeSource.BUSINESS_PROFESSION in inferred.answers.incomeSources.orEmpty())
        assertEquals(
            setOf(BusinessKind.PARTNER_IN_FIRM, BusinessKind.REGULAR_BOOKS),
            inferred.answers.businessKinds
        )
    }

    @Test
    fun cryptoAndForeignPapersSetTheSituationsThatChangeTheForm() {
        val inferred = AnswersFromDocuments.infer(
            parse(
                "vda.pdf" to DocumentSamples.cryptoStatement,
                "form67.pdf" to DocumentSamples.foreignTaxProof,
                "itrv.pdf" to DocumentSamples.lastYearReturn
            )
        )

        assertTrue(IncomeSource.VIRTUAL_DIGITAL_ASSETS in inferred.answers.incomeSources.orEmpty())
        assertTrue(SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF in inferred.answers.specialSituations.orEmpty())
        assertTrue(SpecialSituation.CARRIED_FORWARD_LOSS in inferred.answers.specialSituations.orEmpty())
    }

    @Test
    fun theDocumentsCarryStraightThroughToAFormRecommendation() {
        val inferred = AnswersFromDocuments.infer(
            parse(
                "form16.pdf" to DocumentSamples.form16,
                "ais.pdf" to DocumentSamples.ais
            ),
            base = OnboardingAnswers(residentialStatus = ResidentialStatus.RESIDENT)
        )
        val answers = inferred.answers.copy(specialSituations = emptySet())

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun nothingImportedChangesNothing() {
        val base = OnboardingAnswers(residentialStatus = ResidentialStatus.RESIDENT)

        val inferred = AnswersFromDocuments.infer(emptyList(), base)

        assertEquals(base, inferred.answers)
        assertTrue(inferred.findings.isEmpty())
        assertNull(inferred.estimatedTotalIncome)
    }
}