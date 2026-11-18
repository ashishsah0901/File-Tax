package com.ashish.filetax.onboarding

import com.ashish.filetax.onboarding.data.InMemoryKeyValueStore
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.logic.FilingGuide
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FilingGuideTest {

    @Test
    fun everyFormGetsAWalkthroughFromLoginToVerification() {
        ItrForm.entries.forEach { form ->
            val steps = FilingGuide.forForm(form, everythingAnswers())

            assertTrue(steps.isNotEmpty(), "${form.label} has no steps")
            assertTrue(steps.first().id.endsWith("_must_you_file"))
            assertTrue(steps.any { it.id.endsWith("_login") }, "${form.label} never says to log in")
            assertTrue(steps.any { it.id.endsWith("_pay_tax") }, "${form.label} never says to pay the balance")
            assertTrue(steps.any { it.id.endsWith("_everify") }, "${form.label} never says to verify")
            assertTrue(steps.last().id.endsWith("_deadlines"))
        }
    }

    @Test
    fun theSalaryStepOnlyAppearsForSomeoneWithASalary() {
        val salaried = FilingGuide.forForm(ItrForm.ITR1, salariedAnswers())
        val interestOnly = FilingGuide.forForm(
            ItrForm.ITR1,
            answersWith(IncomeSource.INTEREST_DIVIDEND_OTHER)
        )

        assertTrue(salaried.any { it.id == "itr1_salary" })
        assertFalse(interestOnly.any { it.id == "itr1_salary" })
    }

    @Test
    fun theHousePropertyStepOnlyAppearsForSomeoneWithAHouse() {
        val withHouse = FilingGuide.forForm(
            ItrForm.ITR1,
            salariedAnswers().copy(
                incomeSources = setOf(IncomeSource.SALARY_PENSION, IncomeSource.HOUSE_PROPERTY),
                housePropertyCount = HousePropertyCount.ONE
            )
        )
        val withoutHouse = FilingGuide.forForm(ItrForm.ITR1, salariedAnswers())

        assertTrue(withHouse.any { it.id == "itr1_house_property" })
        assertFalse(withoutHouse.any { it.id == "itr1_house_property" })
    }

    @Test
    fun itrTwoEntersCapitalGainsOnlyWhenThereAreAny() {
        val withGains = answersWith(IncomeSource.SALARY_PENSION, IncomeSource.CAPITAL_GAINS).copy(
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_SHORT_TERM)
        )

        assertTrue(FilingGuide.forForm(ItrForm.ITR2, withGains).any { it.id == "itr2_capital_gains" })
        assertFalse(FilingGuide.forForm(ItrForm.ITR2, salariedAnswers()).any { it.id == "itr2_capital_gains" })
    }

    @Test
    fun foreignHoldingsGetTheirOwnStepsOnItrTwo() {
        val answers = salariedAnswers().copy(
            specialSituations = setOf(
                SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS,
                SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF,
                SpecialSituation.CARRIED_FORWARD_LOSS
            )
        )

        val ids = FilingGuide.forForm(ItrForm.ITR2, answers).map { it.id }

        assertTrue("itr2_foreign_assets" in ids)
        assertTrue("itr2_foreign_tax" in ids)
        assertTrue("itr2_losses" in ids)
    }

    @Test
    fun itrThreeWalksThroughTheBooksAndTheFirm() {
        val answers = answersWith(IncomeSource.BUSINESS_PROFESSION).copy(
            businessKinds = setOf(
                BusinessKind.REGULAR_BOOKS,
                BusinessKind.SPECULATIVE_OR_DERIVATIVES,
                BusinessKind.PARTNER_IN_FIRM
            )
        )

        val ids = FilingGuide.forForm(ItrForm.ITR3, answers).map { it.id }

        assertTrue("itr3_business_books" in ids)
        assertTrue("itr3_trading" in ids)
        assertTrue("itr3_partner" in ids)
        assertFalse("itr3_presumptive" in ids)
    }

    @Test
    fun itrFourDeclaresPresumptiveIncome() {
        val answers = answersWith(IncomeSource.BUSINESS_PROFESSION).copy(
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_BUSINESS_44AD)
        )

        val ids = FilingGuide.forForm(ItrForm.ITR4, answers).map { it.id }

        assertTrue("itr4_presumptive" in ids)
        assertFalse(ids.any { it.contains("capital_gains") })
    }

    @Test
    fun everyStepSaysWhatToDo() {
        ItrForm.entries.forEach { form ->
            val steps = FilingGuide.forForm(form, everythingAnswers())

            assertTrue(steps.all { it.title.isNotBlank() })
            assertTrue(steps.all { it.detail.isNotBlank() })
        }
    }

    @Test
    fun stepIdsNeverRepeat() {
        ItrForm.entries.forEach { form ->
            val ids = FilingGuide.forForm(form, everythingAnswers()).map { it.id }

            assertEquals(ids.size, ids.toSet().size)
        }
    }

    @Test
    fun everyDocumentAStepAsksForIsOnTheChecklist() {
        val cases = listOf(
            salariedAnswers(),
            everythingAnswers(),
            answersWith(IncomeSource.BUSINESS_PROFESSION).copy(
                businessKinds = setOf(BusinessKind.PRESUMPTIVE_PROFESSION_44ADA)
            )
        )
        cases.forEach { answers ->
            val checklistIds = DocumentChecklist.forAnswers(answers).map { it.id }.toSet()
            ItrForm.entries.forEach { form ->
                val askedFor = FilingGuide.forForm(form, answers).flatMap { it.documentIds }

                assertTrue(askedFor.all { it in checklistIds })
            }
        }
    }

    @Test
    fun tickedOffStepsSurviveAReload() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())

        repository.saveCompletedSteps(setOf("itr1_login", "itr1_start_return"))

        assertEquals(
            setOf("itr1_login", "itr1_start_return"),
            repository.loadCompletedSteps()
        )
    }

    @Test
    fun startingOverForgetsTheProgress() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())
        repository.saveCompletedSteps(setOf("itr1_login"))

        repository.clear()

        assertTrue(repository.loadCompletedSteps().isEmpty())
    }

    private fun salariedAnswers() = answersWith(IncomeSource.SALARY_PENSION)

    private fun answersWith(vararg sources: IncomeSource) = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = sources.toSet(),
        totalIncomeBand = TotalIncomeBand.UP_TO_FIFTY_LAKH,
        specialSituations = emptySet()
    )

    private fun everythingAnswers() = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = IncomeSource.entries.toSet(),
        capitalGainKinds = CapitalGainKind.entries.toSet(),
        housePropertyCount = HousePropertyCount.MORE_THAN_TWO,
        businessKinds = BusinessKind.entries.toSet(),
        agriculturalIncomeAboveLimit = true,
        totalIncomeBand = TotalIncomeBand.ABOVE_ONE_CRORE,
        specialSituations = SpecialSituation.entries.toSet()
    )

    @Test
    fun theGuideCarriesThisYearsDates() {
        val simple = FilingGuide.forForm(ItrForm.ITR1, salariedAnswers())
            .single { it.id == "itr1_deadlines" }
        val business = FilingGuide.forForm(ItrForm.ITR3, everythingAnswers())
            .single { it.id == "itr3_deadlines" }

        assertTrue(simple.detail.contains(TaxYear.DUE_DATE_WITHOUT_BUSINESS))
        assertTrue(business.detail.contains(TaxYear.DUE_DATE_BUSINESS_WITHOUT_AUDIT))
        assertTrue(business.detail.contains(TaxYear.DUE_DATE_WITH_AUDIT))
        listOf(simple, business).forEach { step ->
            assertTrue(step.detail.contains(TaxYear.BELATED_RETURN_DATE))
            assertTrue(step.detail.contains(TaxYear.REVISED_RETURN_DATE))
            assertTrue(step.detail.contains(TaxYear.UPDATED_RETURN_DATE))
        }
    }

    @Test
    fun everyFormSaysWhoHasToFileAtAll() {
        ItrForm.entries.forEach { form ->
            val step = FilingGuide.forForm(form, everythingAnswers()).first()

            assertTrue(step.detail.contains("₹4,00,000"), "${form.label} does not give the exemption limit")
            assertTrue(step.detail.contains("outside India"), "${form.label} misses the foreign asset trigger")
        }
    }
}