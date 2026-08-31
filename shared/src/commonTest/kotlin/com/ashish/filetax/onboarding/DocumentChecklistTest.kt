package com.ashish.filetax.onboarding

import com.ashish.filetax.onboarding.data.InMemoryKeyValueStore
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.logic.DocumentCatalog
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DocumentChecklistTest {

    @Test
    fun everyoneNeedsTheirIdentityAndTheDepartmentsOwnRecords() {
        val ids = idsFor(OnboardingAnswers())

        assertTrue(DocumentCatalog.pan.id in ids)
        assertTrue(DocumentCatalog.aadhaar.id in ids)
        assertTrue(DocumentCatalog.annualInformationStatement.id in ids)
        assertTrue(DocumentCatalog.form26as.id in ids)
        assertTrue(DocumentCatalog.bankAccount.id in ids)
    }

    @Test
    fun aSalariedFilerIsAskedForFormSixteenAndNotABrokerStatement() {
        val ids = idsFor(answersWith(IncomeSource.SALARY_PENSION))

        assertTrue(DocumentCatalog.form16.id in ids)
        assertFalse(DocumentCatalog.brokerStatement.id in ids)
        assertFalse(DocumentCatalog.propertySalePapers.id in ids)
    }

    @Test
    fun sellingSharesAsksForTheBrokerAndFundStatements() {
        val answers = answersWith(IncomeSource.CAPITAL_GAINS).copy(
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)
        )

        val ids = idsFor(answers)

        assertTrue(DocumentCatalog.brokerStatement.id in ids)
        assertTrue(DocumentCatalog.mutualFundStatement.id in ids)
        assertFalse(DocumentCatalog.propertySalePapers.id in ids)
    }

    @Test
    fun sellingAFlatAsksForTheDeeds() {
        val answers = answersWith(IncomeSource.CAPITAL_GAINS).copy(
            capitalGainKinds = setOf(CapitalGainKind.LAND_OR_BUILDING)
        )

        assertTrue(DocumentCatalog.propertySalePapers.id in idsFor(answers))
    }

    @Test
    fun presumptiveBusinessAsksForTurnoverButNotBooks() {
        val answers = answersWith(IncomeSource.BUSINESS_PROFESSION).copy(
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_BUSINESS_44AD)
        )

        val ids = idsFor(answers)

        assertTrue(DocumentCatalog.turnoverSummary.id in ids)
        assertTrue(DocumentCatalog.businessBankStatements.id in ids)
        assertFalse(DocumentCatalog.booksOfAccounts.id in ids)
    }

    @Test
    fun realProfitAsksForTheAccounts() {
        val answers = answersWith(IncomeSource.BUSINESS_PROFESSION).copy(
            businessKinds = setOf(BusinessKind.REGULAR_BOOKS)
        )

        assertTrue(DocumentCatalog.booksOfAccounts.id in idsFor(answers))
    }

    @Test
    fun tradingAsksForBothTheAccountsAndTheBrokerStatementOnce() {
        val answers = answersWith(IncomeSource.BUSINESS_PROFESSION, IncomeSource.CAPITAL_GAINS).copy(
            businessKinds = setOf(BusinessKind.SPECULATIVE_OR_DERIVATIVES),
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_SHORT_TERM)
        )

        val ids = idsFor(answers)

        assertTrue(DocumentCatalog.booksOfAccounts.id in ids)
        assertEquals(1, ids.count { it == DocumentCatalog.brokerStatement.id })
    }

    @Test
    fun foreignHoldingsAndOldLossesEachAddTheirOwnDocument() {
        val answers = answersWith(IncomeSource.SALARY_PENSION).copy(
            specialSituations = setOf(
                SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS,
                SpecialSituation.CARRIED_FORWARD_LOSS
            )
        )

        val ids = idsFor(answers)

        assertTrue(DocumentCatalog.foreignAssetDetails.id in ids)
        assertTrue(DocumentCatalog.lastYearReturn.id in ids)
    }

    @Test
    fun theListNeverRepeatsADocument() {
        val answers = OnboardingAnswers(
            residentialStatus = ResidentialStatus.RESIDENT,
            incomeSources = IncomeSource.entries.toSet(),
            capitalGainKinds = CapitalGainKind.entries.toSet(),
            businessKinds = BusinessKind.entries.toSet(),
            agriculturalIncomeAboveLimit = true,
            totalIncomeAboveFiftyLakh = true,
            specialSituations = SpecialSituation.entries.toSet()
        )

        val ids = idsFor(answers)

        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun everyDocumentSaysWhatItIsForAndWhereToGetIt() {
        val documents = DocumentChecklist.forAnswers(
            OnboardingAnswers(
                incomeSources = IncomeSource.entries.toSet(),
                capitalGainKinds = CapitalGainKind.entries.toSet(),
                businessKinds = BusinessKind.entries.toSet(),
                agriculturalIncomeAboveLimit = true,
                specialSituations = SpecialSituation.entries.toSet()
            )
        )

        assertTrue(documents.all { it.name.isNotBlank() })
        assertTrue(documents.all { it.why.isNotBlank() })
        assertTrue(documents.all { it.whereToGet.isNotBlank() })
    }

    @Test
    fun tickedOffDocumentsSurviveAReload() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())

        repository.saveCollectedDocuments(setOf(DocumentCatalog.pan.id, DocumentCatalog.form16.id))

        assertEquals(
            setOf(DocumentCatalog.pan.id, DocumentCatalog.form16.id),
            repository.loadCollectedDocuments()
        )
    }

    @Test
    fun startingOverForgetsWhatWasCollected() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())
        repository.saveCollectedDocuments(setOf(DocumentCatalog.pan.id))

        repository.clear()

        assertTrue(repository.loadCollectedDocuments().isEmpty())
    }

    private fun idsFor(answers: OnboardingAnswers) =
        DocumentChecklist.forAnswers(answers).map { it.id }

    private fun answersWith(vararg sources: IncomeSource) = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = sources.toSet()
    )
}