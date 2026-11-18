package com.ashish.filetax.onboarding.model

import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.tutorial.TutorialAnimation

data class SchedulePick(
    val name: String,
    val why: String
)

data class FilingStep(
    val id: String,
    val title: String,
    val detail: String,
    val tip: String,
    val animation: TutorialAnimation,
    val schedules: List<ItrSchedule> = emptyList(),
    val schedulePicks: List<SchedulePick> = emptyList(),
    val documentIds: List<String> = emptyList()
)