package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.TrackableStep
import com.ashish.filetax.onboarding.model.TaxYear

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
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "${state.completedCount} of ${state.steps.size} done",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "How to file ${state.form.label}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Work through these in order on incometax.gov.in. Each step says which of your documents to keep open.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(20.dp))
        state.steps.forEachIndexed { index, item ->
            StepCard(
                number = index + 1,
                item = item,
                onClick = { onStepToggled(item.step.id) }
            )
        }
        Text(
            text = "The portal saves a draft as you go, so you can stop and come back. This is guidance for a typical ITR-1, not tax advice.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
    }
}

@Composable
private fun StepCard(number: Int, item: TrackableStep, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (item.completed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 12.dp, top = 14.dp, bottom = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.step.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
                Checkbox(checked = item.completed, onCheckedChange = null)
            }
            Text(
                text = item.step.detail,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, end = 4.dp)
            )
            if (item.documents.isNotEmpty()) {
                Row(modifier = Modifier.padding(top = 10.dp, end = 4.dp)) {
                    Text(
                        text = "Have ready",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.documents.joinToString { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
