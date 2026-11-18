package com.ashish.filetax.onboarding.model

enum class ItrForm(val label: String, val summary: String) {
    ITR1(
        label = "ITR-1 (Sahaj)",
        summary = "The shortest return. Built for a resident with salary or pension, one house, ordinary interest income and little else."
    ),
    ITR2(
        label = "ITR-2",
        summary = "For a resident or non-resident with no business income, but capital gains, more than one house, foreign assets or a larger income."
    ),
    ITR3(
        label = "ITR-3",
        summary = "The full return for anyone with business or professional income declared from books, trading income, or a share in a partnership firm."
    ),
    ITR4(
        label = "ITR-4 (Sugam)",
        summary = "For a resident declaring business or professional income at a fixed presumptive rate instead of maintaining books."
    )
}

data class ItrRecommendation(
    val form: ItrForm,
    val reasons: List<String>,
    val alternative: String? = null,
    val cautions: List<String> = emptyList()
)