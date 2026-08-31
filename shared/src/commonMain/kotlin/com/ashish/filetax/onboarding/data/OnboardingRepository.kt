package com.ashish.filetax.onboarding.data

import com.ashish.filetax.onboarding.model.OnboardingAnswers
import kotlinx.serialization.json.Json

class OnboardingRepository(private val store: KeyValueStore) {

    private val json = Json { ignoreUnknownKeys = true }

    fun load(): OnboardingAnswers {
        val stored = store.read(KEY_ANSWERS) ?: return OnboardingAnswers()
        val answers = runCatching { json.decodeFromString<OnboardingAnswers>(stored) }.getOrNull()
        return answers?.takeIf { it.assessmentYear == OnboardingAnswers().assessmentYear }
            ?: OnboardingAnswers()
    }

    fun save(answers: OnboardingAnswers) {
        store.write(KEY_ANSWERS, json.encodeToString(answers))
    }

    fun loadCollectedDocuments(): Set<String> {
        val stored = store.read(KEY_COLLECTED_DOCUMENTS) ?: return emptySet()
        return runCatching { json.decodeFromString<Set<String>>(stored) }.getOrNull().orEmpty()
    }

    fun saveCollectedDocuments(ids: Set<String>) {
        store.write(KEY_COLLECTED_DOCUMENTS, json.encodeToString(ids))
    }

    fun loadCompletedSteps(): Set<String> {
        val stored = store.read(KEY_COMPLETED_STEPS) ?: return emptySet()
        return runCatching { json.decodeFromString<Set<String>>(stored) }.getOrNull().orEmpty()
    }

    fun saveCompletedSteps(ids: Set<String>) {
        store.write(KEY_COMPLETED_STEPS, json.encodeToString(ids))
    }

    fun clear() {
        store.remove(KEY_ANSWERS)
        store.remove(KEY_COLLECTED_DOCUMENTS)
        store.remove(KEY_COMPLETED_STEPS)
    }

    private companion object {
        const val KEY_ANSWERS = "onboarding_answers"
        const val KEY_COLLECTED_DOCUMENTS = "collected_documents"
        const val KEY_COMPLETED_STEPS = "completed_filing_steps"
    }
}