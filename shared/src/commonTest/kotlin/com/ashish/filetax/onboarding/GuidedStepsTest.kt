package com.ashish.filetax.onboarding

import com.ashish.filetax.documents.DocumentSamples
import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.onboarding.data.InMemoryKeyValueStore
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GuidedStepsTest {

    private val answers = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = setOf(IncomeSource.SALARY_PENSION, IncomeSource.INTEREST_DIVIDEND_OTHER),
        totalIncomeBand = TotalIncomeBand.UP_TO_FIFTY_LAKH,
        specialSituations = emptySet()
    )

    private fun viewModel(): OnboardingViewModel {
        val repository = OnboardingRepository(InMemoryKeyValueStore())
        repository.save(answers)
        return OnboardingViewModel(repository)
    }

    private fun stepsOf(viewModel: OnboardingViewModel): OnboardingUiState.FilingSteps {
        viewModel.onShowDocuments()
        viewModel.onShowFilingSteps()
        return viewModel.uiState as OnboardingUiState.FilingSteps
    }

    @Test
    fun theGuideOpensOnTheFirstStepAndNothingElse() {
        val state = stepsOf(viewModel())

        assertEquals(0, state.currentIndex)
        assertEquals(0, state.completedCount)
        assertTrue(state.steps.size > 8)
    }

    @Test
    fun finishingAStepOpensTheNextOne() {
        val viewModel = viewModel()
        val state = stepsOf(viewModel)

        viewModel.onStepToggled(state.steps[0].step.id)
        val next = viewModel.uiState as OnboardingUiState.FilingSteps

        assertEquals(1, next.currentIndex)
        assertEquals(1, next.completedCount)
    }

    @Test
    fun oneStepAsksYouToTickTheSchedules() {
        val state = stepsOf(viewModel())

        val step = state.steps.single { it.step.id.endsWith("_schedules") }

        assertTrue(step.step.schedulePicks.isNotEmpty())
        assertTrue(step.step.schedulePicks.any { it.name.contains("Part A") })
    }

    @Test
    fun theFiguresFromYourDocumentsLandOnTheStepThatNeedsThem() {
        val viewModel = viewModel()
        viewModel.onUseDocuments(
            listOf(
                DocumentScanner.parse("form16.pdf", DocumentSamples.form16),
                DocumentScanner.parse("26as.pdf", DocumentSamples.form26as)
            )
        )
        val state = stepsOf(viewModel)

        val salaryStep = state.steps.single { it.step.id.endsWith("_salary") }
        val taxesStep = state.steps.single { it.step.id.endsWith("_taxes_paid") }

        assertTrue(state.hasDocuments)
        assertTrue(salaryStep.step.schedules.contains(ItrSchedule.SALARY))
        assertTrue(salaryStep.values.any { it.figure.kind.label == "Gross salary" })
        assertTrue(taxesStep.values.any { it.figure.kind.label.contains("TDS") })
        assertTrue(salaryStep.values.none { it.field.schedule != ItrSchedule.SALARY })
    }

    @Test
    fun withNoDocumentsTheStepsStillStandOnTheirOwn() {
        val state = stepsOf(viewModel())

        assertTrue(state.steps.all { it.values.isEmpty() && it.details.isEmpty() })
        assertTrue(state.steps.all { it.step.detail.isNotBlank() })
    }
}