package com.ashish.filetax.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ashish.filetax.onboarding.data.OnboardingRepository
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.logic.FilingGuide
import com.ashish.filetax.onboarding.logic.ItrFormDeterminer
import com.ashish.filetax.onboarding.logic.OnboardingFlow
import com.ashish.filetax.onboarding.model.FilingStep
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.ItrRecommendation
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.Question
import com.ashish.filetax.onboarding.model.TaxDocument

sealed interface OnboardingUiState {

    data class Asking(
        val question: Question,
        val selection: Set<String>,
        val noneSelected: Boolean,
        val stepNumber: Int,
        val stepCount: Int,
        val canGoBack: Boolean,
        val canContinue: Boolean,
        val isLastStep: Boolean
    ) : OnboardingUiState

    data class Result(val recommendation: ItrRecommendation) : OnboardingUiState

    data class Documents(
        val form: ItrForm,
        val documents: List<CollectableDocument>,
        val collectedCount: Int,
        val hasFilingGuide: Boolean
    ) : OnboardingUiState

    data class FilingSteps(
        val form: ItrForm,
        val steps: List<TrackableStep>,
        val completedCount: Int
    ) : OnboardingUiState
}

data class CollectableDocument(
    val document: TaxDocument,
    val collected: Boolean
)

data class TrackableStep(
    val step: FilingStep,
    val documents: List<TaxDocument>,
    val completed: Boolean
)

class OnboardingViewModel(private val repository: OnboardingRepository) : ViewModel() {

    private var answers by mutableStateOf(repository.load())
    private var cursor by mutableStateOf(0)
    private var showingResult by mutableStateOf(false)
    private var showingDocuments by mutableStateOf(false)
    private var showingFilingSteps by mutableStateOf(false)
    private var collectedDocuments by mutableStateOf(repository.loadCollectedDocuments())
    private var completedSteps by mutableStateOf(repository.loadCompletedSteps())

    init {
        if (OnboardingFlow.isComplete(answers)) {
            showingResult = true
            cursor = OnboardingFlow.questions(answers).lastIndex.coerceAtLeast(0)
        } else {
            cursor = OnboardingFlow.firstUnansweredIndex(answers)
        }
    }

    val uiState: OnboardingUiState
        get() {
            val questions = OnboardingFlow.questions(answers)
            if (showingFilingSteps) {
                val form = ItrFormDeterminer.determine(answers).form
                val checklist = DocumentChecklist.forAnswers(answers).associateBy { it.id }
                val steps = FilingGuide.forForm(form, answers).map { step ->
                    TrackableStep(
                        step = step,
                        documents = step.documentIds.mapNotNull(checklist::get),
                        completed = step.id in completedSteps
                    )
                }
                return OnboardingUiState.FilingSteps(
                    form = form,
                    steps = steps,
                    completedCount = steps.count { it.completed }
                )
            }
            if (showingDocuments) {
                val form = ItrFormDeterminer.determine(answers).form
                val documents = DocumentChecklist.forAnswers(answers).map {
                    CollectableDocument(document = it, collected = it.id in collectedDocuments)
                }
                return OnboardingUiState.Documents(
                    form = form,
                    documents = documents,
                    collectedCount = documents.count { it.collected },
                    hasFilingGuide = FilingGuide.forForm(form, answers).isNotEmpty()
                )
            }
            if (showingResult) {
                return OnboardingUiState.Result(ItrFormDeterminer.determine(answers))
            }
            val index = cursor.coerceIn(0, questions.lastIndex)
            val question = questions[index]
            val answered = OnboardingFlow.isAnswered(answers, question.id)
            val selection = OnboardingFlow.selection(answers, question.id)
            return OnboardingUiState.Asking(
                question = question,
                selection = selection,
                noneSelected = answered && selection.isEmpty(),
                stepNumber = index + 1,
                stepCount = questions.size,
                canGoBack = index > 0,
                canContinue = answered,
                isLastStep = index == questions.lastIndex
            )
        }

    fun onChoiceSelected(key: String) {
        val question = currentQuestion() ?: return
        val selection = when (question) {
            is Question.SingleChoice -> setOf(key)
            is Question.MultipleChoice -> {
                val current = OnboardingFlow.selection(answers, question.id)
                if (key in current) current - key else current + key
            }
        }
        update(OnboardingFlow.answer(answers, question.id, selection))
    }

    fun onNoneOfTheseSelected() {
        val question = currentQuestion() ?: return
        update(OnboardingFlow.answer(answers, question.id, emptySet()))
    }

    fun onContinue() {
        val questions = OnboardingFlow.questions(answers)
        if (cursor >= questions.lastIndex) {
            showingResult = true
        } else {
            cursor += 1
        }
    }

    fun onShowDocuments() {
        showingDocuments = true
    }

    fun onShowFilingSteps() {
        showingFilingSteps = true
    }

    fun onDocumentToggled(id: String) {
        val updated = if (id in collectedDocuments) collectedDocuments - id else collectedDocuments + id
        collectedDocuments = updated
        repository.saveCollectedDocuments(updated)
    }

    fun onStepToggled(id: String) {
        val updated = if (id in completedSteps) completedSteps - id else completedSteps + id
        completedSteps = updated
        repository.saveCompletedSteps(updated)
    }

    fun onBack() {
        if (showingFilingSteps) {
            showingFilingSteps = false
        } else if (showingDocuments) {
            showingDocuments = false
        } else if (showingResult) {
            showingResult = false
            cursor = OnboardingFlow.questions(answers).lastIndex.coerceAtLeast(0)
        } else if (cursor > 0) {
            cursor -= 1
        }
    }

    fun onRestart() {
        repository.clear()
        answers = OnboardingAnswers()
        collectedDocuments = emptySet()
        completedSteps = emptySet()
        cursor = 0
        showingResult = false
        showingDocuments = false
        showingFilingSteps = false
    }

    private fun currentQuestion(): Question? =
        OnboardingFlow.questions(answers).getOrNull(cursor)

    private fun update(updated: OnboardingAnswers) {
        answers = updated
        repository.save(updated)
    }
}