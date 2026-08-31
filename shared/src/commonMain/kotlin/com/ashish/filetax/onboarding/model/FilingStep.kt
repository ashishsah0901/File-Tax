package com.ashish.filetax.onboarding.model

data class FilingStep(
    val id: String,
    val title: String,
    val detail: String,
    val documentIds: List<String> = emptyList()
)
