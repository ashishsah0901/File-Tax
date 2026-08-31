package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BooleanChoice
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.Question
import com.ashish.filetax.onboarding.model.QuestionId
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation

object OnboardingFlow {

    fun questions(answers: OnboardingAnswers): List<Question> =
        applicableIds(answers).map(OnboardingQuestions::byId)

    fun isAnswered(answers: OnboardingAnswers, id: QuestionId): Boolean = when (id) {
        QuestionId.RESIDENTIAL_STATUS -> answers.residentialStatus != null
        QuestionId.INCOME_SOURCES -> answers.incomeSources != null
        QuestionId.CAPITAL_GAIN_KINDS -> !answers.capitalGainKinds.isNullOrEmpty()
        QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT -> answers.equityLongTermGainWithinExemptLimit != null
        QuestionId.HOUSE_PROPERTY_COUNT -> answers.housePropertyCount != null
        QuestionId.BUSINESS_KINDS -> !answers.businessKinds.isNullOrEmpty()
        QuestionId.AGRICULTURAL_INCOME_LIMIT -> answers.agriculturalIncomeAboveLimit != null
        QuestionId.TOTAL_INCOME_BAND -> answers.totalIncomeAboveFiftyLakh != null
        QuestionId.SPECIAL_SITUATIONS -> answers.specialSituations != null
    }

    fun isComplete(answers: OnboardingAnswers): Boolean =
        applicableIds(answers).all { isAnswered(answers, it) }

    fun firstUnansweredIndex(answers: OnboardingAnswers): Int {
        val ids = applicableIds(answers)
        val index = ids.indexOfFirst { !isAnswered(answers, it) }
        return if (index == -1) ids.lastIndex.coerceAtLeast(0) else index
    }

    fun selection(answers: OnboardingAnswers, id: QuestionId): Set<String> = when (id) {
        QuestionId.RESIDENTIAL_STATUS -> setOfNotNull(answers.residentialStatus?.name)
        QuestionId.INCOME_SOURCES -> answers.incomeSources.orEmpty().map { it.name }.toSet()
        QuestionId.CAPITAL_GAIN_KINDS -> answers.capitalGainKinds.orEmpty().map { it.name }.toSet()
        QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT -> booleanSelection(answers.equityLongTermGainWithinExemptLimit)
        QuestionId.HOUSE_PROPERTY_COUNT -> setOfNotNull(answers.housePropertyCount?.name)
        QuestionId.BUSINESS_KINDS -> answers.businessKinds.orEmpty().map { it.name }.toSet()
        QuestionId.AGRICULTURAL_INCOME_LIMIT -> booleanSelection(answers.agriculturalIncomeAboveLimit)
        QuestionId.TOTAL_INCOME_BAND -> booleanSelection(answers.totalIncomeAboveFiftyLakh)
        QuestionId.SPECIAL_SITUATIONS -> answers.specialSituations.orEmpty().map { it.name }.toSet()
    }

    fun answer(answers: OnboardingAnswers, id: QuestionId, keys: Set<String>): OnboardingAnswers {
        val updated = when (id) {
            QuestionId.RESIDENTIAL_STATUS -> answers.copy(
                residentialStatus = keys.firstOrNull()?.let(ResidentialStatus::valueOf)
            )

            QuestionId.INCOME_SOURCES -> answers.copy(
                incomeSources = keys.map(IncomeSource::valueOf).toSet()
            )

            QuestionId.CAPITAL_GAIN_KINDS -> answers.copy(
                capitalGainKinds = keys.map(CapitalGainKind::valueOf).toSet()
            )

            QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT -> answers.copy(
                equityLongTermGainWithinExemptLimit = keys.toBoolean()
            )

            QuestionId.HOUSE_PROPERTY_COUNT -> answers.copy(
                housePropertyCount = keys.firstOrNull()?.let(HousePropertyCount::valueOf)
            )

            QuestionId.BUSINESS_KINDS -> answers.copy(
                businessKinds = keys.map(BusinessKind::valueOf).toSet()
            )

            QuestionId.AGRICULTURAL_INCOME_LIMIT -> answers.copy(
                agriculturalIncomeAboveLimit = keys.toBoolean()
            )

            QuestionId.TOTAL_INCOME_BAND -> answers.copy(
                totalIncomeAboveFiftyLakh = keys.toBoolean()
            )

            QuestionId.SPECIAL_SITUATIONS -> answers.copy(
                specialSituations = keys.map(SpecialSituation::valueOf).toSet()
            )
        }
        return prune(updated)
    }

    private fun prune(answers: OnboardingAnswers): OnboardingAnswers {
        val applicable = applicableIds(answers).toSet()
        var pruned = answers
        if (QuestionId.CAPITAL_GAIN_KINDS !in applicable) {
            pruned = pruned.copy(capitalGainKinds = null)
        }
        if (QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT !in applicable) {
            pruned = pruned.copy(equityLongTermGainWithinExemptLimit = null)
        }
        if (QuestionId.HOUSE_PROPERTY_COUNT !in applicable) {
            pruned = pruned.copy(housePropertyCount = null)
        }
        if (QuestionId.BUSINESS_KINDS !in applicable) {
            pruned = pruned.copy(businessKinds = null)
        }
        if (QuestionId.AGRICULTURAL_INCOME_LIMIT !in applicable) {
            pruned = pruned.copy(agriculturalIncomeAboveLimit = null)
        }
        return pruned
    }

    private fun applicableIds(answers: OnboardingAnswers): List<QuestionId> {
        val ids = mutableListOf(QuestionId.RESIDENTIAL_STATUS, QuestionId.INCOME_SOURCES)
        val sources = answers.incomeSources ?: return ids
        if (IncomeSource.CAPITAL_GAINS in sources) {
            ids += QuestionId.CAPITAL_GAIN_KINDS
            if (answers.capitalGainKinds == setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)) {
                ids += QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT
            }
        }
        if (IncomeSource.HOUSE_PROPERTY in sources) {
            ids += QuestionId.HOUSE_PROPERTY_COUNT
        }
        if (IncomeSource.BUSINESS_PROFESSION in sources) {
            ids += QuestionId.BUSINESS_KINDS
        }
        if (IncomeSource.AGRICULTURE in sources) {
            ids += QuestionId.AGRICULTURAL_INCOME_LIMIT
        }
        ids += QuestionId.TOTAL_INCOME_BAND
        ids += QuestionId.SPECIAL_SITUATIONS
        return ids
    }

    private fun booleanSelection(value: Boolean?): Set<String> = when (value) {
        true -> setOf(BooleanChoice.YES)
        false -> setOf(BooleanChoice.NO)
        null -> emptySet()
    }

    private fun Set<String>.toBoolean(): Boolean? = when {
        BooleanChoice.YES in this -> true
        BooleanChoice.NO in this -> false
        else -> null
    }
}