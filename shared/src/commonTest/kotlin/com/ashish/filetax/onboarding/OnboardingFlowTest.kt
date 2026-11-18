package com.ashish.filetax.onboarding

import com.ashish.filetax.onboarding.data.InMemoryKeyValueStore
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.logic.OnboardingFlow
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.QuestionId
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OnboardingFlowTest {

    @Test
    fun startsWithResidencyAndIncomeSourcesOnly() {
        val ids = OnboardingFlow.questions(OnboardingAnswers()).map { it.id }

        assertEquals(listOf(QuestionId.RESIDENTIAL_STATUS, QuestionId.INCOME_SOURCES), ids)
    }

    @Test
    fun capitalGainDetailIsAskedOnlyWhenSomethingWasSold() {
        val withoutSale = answersWith(IncomeSource.SALARY_PENSION)
        val withSale = answersWith(IncomeSource.CAPITAL_GAINS)

        assertFalse(OnboardingFlow.questions(withoutSale).any { it.id == QuestionId.CAPITAL_GAIN_KINDS })
        assertTrue(OnboardingFlow.questions(withSale).any { it.id == QuestionId.CAPITAL_GAIN_KINDS })
    }

    @Test
    fun exemptLimitIsAskedOnlyForListedEquityLongTermGains() {
        val equityOnly = answersWith(IncomeSource.CAPITAL_GAINS).copy(
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)
        )
        val mixed = equityOnly.copy(
            capitalGainKinds = setOf(
                CapitalGainKind.LISTED_EQUITY_LONG_TERM,
                CapitalGainKind.LAND_OR_BUILDING
            )
        )

        assertTrue(OnboardingFlow.questions(equityOnly).any { it.id == QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT })
        assertFalse(OnboardingFlow.questions(mixed).any { it.id == QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT })
    }

    @Test
    fun droppingAnIncomeSourceForgetsItsFollowUpAnswers() {
        val withSale = answersWith(IncomeSource.SALARY_PENSION, IncomeSource.CAPITAL_GAINS).copy(
            capitalGainKinds = setOf(CapitalGainKind.LAND_OR_BUILDING)
        )

        val withoutSale = OnboardingFlow.answer(
            answers = withSale,
            id = QuestionId.INCOME_SOURCES,
            keys = setOf(IncomeSource.SALARY_PENSION.name)
        )

        assertNull(withoutSale.capitalGainKinds)
    }

    @Test
    fun noneOfTheseIsAnAnswer() {
        val answered = OnboardingFlow.answer(OnboardingAnswers(), QuestionId.INCOME_SOURCES, emptySet())

        assertTrue(OnboardingFlow.isAnswered(answered, QuestionId.INCOME_SOURCES))
    }

    @Test
    fun flowIsCompleteOnlyWhenEveryApplicableQuestionIsAnswered() {
        val partial = answersWith(IncomeSource.SALARY_PENSION)
        val complete = partial.copy(totalIncomeBand = TotalIncomeBand.UP_TO_FIFTY_LAKH, specialSituations = emptySet())

        assertFalse(OnboardingFlow.isComplete(partial))
        assertTrue(OnboardingFlow.isComplete(complete))
    }

    @Test
    fun answersSurviveAReload() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())
        val answers = answersWith(IncomeSource.SALARY_PENSION)

        repository.save(answers)

        assertEquals(answers, repository.load())
    }

    @Test
    fun clearingForgetsEverything() {
        val repository = OnboardingRepository(InMemoryKeyValueStore())
        repository.save(answersWith(IncomeSource.SALARY_PENSION))

        repository.clear()

        assertEquals(OnboardingAnswers(), repository.load())
    }

    private fun answersWith(vararg sources: IncomeSource) = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = sources.toSet()
    )
}