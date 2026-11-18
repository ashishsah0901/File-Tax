package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.filetax.documents.logic.IndianAmountFormat
import com.ashish.filetax.documents.model.DetailEntry
import com.ashish.filetax.documents.model.ReportEntry
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.TrackableStep
import com.ashish.filetax.onboarding.model.SchedulePick
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.tutorial.ui.TutorialIllustration
import com.ashish.filetax.ui.components.AppCard
import com.ashish.filetax.ui.components.InlineArt
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_check_mark_button

@Composable
internal fun ColumnScope.FilingStepsContent(
    state: OnboardingUiState.FilingSteps,
    onBack: () -> Unit,
    onStepToggled: (String) -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = true,
        onBack = onBack
    )
    LinearProgressIndicator(
        progress = { state.completedCount.toFloat() / state.steps.size.toFloat() },
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        val finished = state.currentIndex >= state.steps.size
        Text(
            text = if (finished) {
                "All ${state.steps.size} steps done"
            } else {
                "Step ${state.currentIndex + 1} of ${state.steps.size}"
            },
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "Filing ${state.form.label}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "One step at a time, on incometax.gov.in. Finish the one you are on and the next opens, with the figures that step needs.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(20.dp))

        var reopenedId by remember(state.form) { mutableStateOf<String?>(null) }

        state.steps.forEachIndexed { index, item ->
            when {
                index < state.currentIndex -> DoneStepRow(
                    number = index + 1,
                    item = item,
                    reopened = reopenedId == item.step.id,
                    hasDocuments = state.hasDocuments,
                    onToggleReopened = {
                        reopenedId = if (reopenedId == item.step.id) null else item.step.id
                    },
                    onUndo = { onStepToggled(item.step.id) }
                )

                index == state.currentIndex -> CurrentStepCard(
                    number = index + 1,
                    item = item,
                    isLast = index == state.steps.lastIndex,
                    hasDocuments = state.hasDocuments,
                    onDone = { onStepToggled(item.step.id) }
                )

                else -> Unit
            }
        }

        val locked = state.steps.size - state.currentIndex - 1
        if (locked > 0) {
            Text(
                text = if (locked == 1) {
                    "One more step after this one."
                } else {
                    "$locked more steps after this one, each opening as you finish the one before."
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
        if (finished) {
            Text(
                text = "That is the return filed and verified. Keep every document you used for six years, in case the department asks.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Text(
            text = "The portal saves a draft as you go, so you can stop and come back. This is guidance, not tax advice.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        )
    }
}

@Composable
private fun CurrentStepCard(
    number: Int,
    item: TrackableStep,
    isLast: Boolean,
    hasDocuments: Boolean,
    onDone: () -> Unit
) {
    AppCard(
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            text = "Step $number",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = item.step.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            TutorialIllustration(
                animation = item.step.animation,
                height = 132.dp
            )
            Text(
                text = item.step.animation.caption,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
        Text(
            text = item.step.detail,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp)
        )
        LabelledLine(label = "On the portal", text = item.step.tip)
        if (item.step.schedulePicks.isNotEmpty()) {
            SectionLabel("Tick these")
            item.step.schedulePicks.forEach { SchedulePickRow(it) }
        }
        if (item.values.isNotEmpty() || item.details.isNotEmpty()) {
            SectionLabel("What to type in this step")
            item.details.forEach { DetailRow(it) }
            item.values.forEach { ValueRow(it) }
        } else if (!hasDocuments && item.step.schedules.isNotEmpty()) {
            Text(
                text = "Import your documents on the previous screen and the figures for this step appear here, with the box each one goes into.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
        if (item.documents.isNotEmpty()) {
            LabelledLine(label = "Have ready", text = item.documents.joinToString { it.name })
        }
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 20.dp)
        ) {
            Text(if (isLast) "Done, that was the last step" else "Done, open the next step")
        }
    }
}

@Composable
private fun DoneStepRow(
    number: Int,
    item: TrackableStep,
    reopened: Boolean,
    hasDocuments: Boolean,
    onToggleReopened: () -> Unit,
    onUndo: () -> Unit
) {
    AppCard(
        onClick = onToggleReopened,
        color = MaterialTheme.colorScheme.primaryContainer,
        outlined = false,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$number",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.step.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 12.dp, end = 12.dp)
            )
            InlineArt(resource = Res.drawable.img_check_mark_button)
        }
        if (reopened) {
            Text(
                text = item.step.detail,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 10.dp)
            )
            if (hasDocuments) {
                item.details.forEach { DetailRow(it) }
                item.values.forEach { ValueRow(it) }
            }
            TextButton(onClick = onUndo, modifier = Modifier.padding(top = 4.dp)) {
                Text("Not done after all")
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
    )
}

@Composable
private fun LabelledLine(label: String, text: String) {
    Row(modifier = Modifier.padding(top = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SchedulePickRow(pick: SchedulePick) {
    Row(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 10.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = pick.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = pick.why,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun ValueRow(entry: ReportEntry) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.figure.kind.label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = IndianAmountFormat.format(entry.figure.amount),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "${entry.field.field} · from ${entry.source.label}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun DetailRow(entry: DetailEntry) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = entry.detail.kind.label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = entry.detail.value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Text(
            text = "${entry.field.field} · from ${entry.source.label}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}