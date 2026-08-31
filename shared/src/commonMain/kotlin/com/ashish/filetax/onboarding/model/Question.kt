package com.ashish.filetax.onboarding.model

enum class QuestionId {
    RESIDENTIAL_STATUS,
    INCOME_SOURCES,
    CAPITAL_GAIN_KINDS,
    EQUITY_LONG_TERM_GAIN_LIMIT,
    HOUSE_PROPERTY_COUNT,
    BUSINESS_KINDS,
    AGRICULTURAL_INCOME_LIMIT,
    TOTAL_INCOME_BAND,
    SPECIAL_SITUATIONS
}

data class Choice(
    val key: String,
    val label: String,
    val hint: String? = null
)

sealed interface Question {
    val id: QuestionId
    val title: String
    val subtitle: String?
    val choices: List<Choice>

    data class SingleChoice(
        override val id: QuestionId,
        override val title: String,
        override val subtitle: String?,
        override val choices: List<Choice>
    ) : Question

    data class MultipleChoice(
        override val id: QuestionId,
        override val title: String,
        override val subtitle: String?,
        override val choices: List<Choice>,
        val noneOfTheseLabel: String? = null
    ) : Question
}

object BooleanChoice {
    const val YES = "YES"
    const val NO = "NO"
}