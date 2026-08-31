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
import androidx.compose.material3.Button
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
import com.ashish.filetax.onboarding.CollectableDocument
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.model.TaxYear

@Composable
internal fun ColumnScope.DocumentsContent(
    state: OnboardingUiState.Documents,
    onBack: () -> Unit,
    onDocumentToggled: (String) -> Unit,
    onShowFilingSteps: () -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = true,
        onBack = onBack
    )
    LinearProgressIndicator(
        progress = { state.collectedCount.toFloat() / state.documents.size.toFloat() },
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "${state.collectedCount} of ${state.documents.size} collected",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "What you need for ${state.form.label}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Only what your answers call for. Tick each one off as you get it.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(20.dp))
        state.documents.forEach { item ->
            DocumentCard(item = item, onClick = { onDocumentToggled(item.document.id) })
        }
        Text(
            text = "Collect these before you open the portal. Filing goes faster when nothing sends you looking mid-way.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
    }
    if (state.hasFilingGuide) {
        Button(
            onClick = onShowFilingSteps,
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
        ) {
            Text("How to file it, step by step")
        }
    }
}

@Composable
private fun DocumentCard(item: CollectableDocument, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (item.collected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 12.dp, top = 14.dp, bottom = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DocumentIllustration(art = item.document.art)
                Text(
                    text = item.document.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f).padding(start = 12.dp)
                )
                Checkbox(checked = item.collected, onCheckedChange = null)
            }
            Text(
                text = item.document.why,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, end = 4.dp)
            )
            Row(modifier = Modifier.padding(top = 10.dp, end = 4.dp)) {
                Text(
                    text = "Where",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.document.whereToGet,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}