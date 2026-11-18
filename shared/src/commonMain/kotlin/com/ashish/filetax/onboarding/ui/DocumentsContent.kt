package com.ashish.filetax.onboarding.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.CollectableDocument
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.ui.components.AppCard
import com.ashish.filetax.ui.components.InlineArt
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_round_pushpin

@Composable
internal fun ColumnScope.DocumentsContent(
    state: OnboardingUiState.Documents,
    onBack: () -> Unit,
    onDocumentToggled: (String) -> Unit,
    onShowFilingSteps: () -> Unit,
    onShowScanner: () -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = true,
        onBack = onBack
    )
    LinearProgressIndicator(
        progress = { state.collectedCount.toFloat() / state.documents.size.toFloat() },
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp)
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
        val transition = rememberInfiniteTransition(label = "documents")
        val motion by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5200, easing = LinearEasing)
            ),
            label = "sheen"
        )
        state.documents.forEach { item ->
            DocumentCard(
                item = item,
                motion = { motion },
                onClick = { onDocumentToggled(item.document.id) }
            )
        }
        Text(
            text = "Collect these before you open the portal. Filing goes faster when nothing sends you looking mid-way.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
    }
    OutlinedButton(
        onClick = onShowScanner,
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 12.dp)
    ) {
        Text("Read the PDFs I already have")
    }
    if (state.hasFilingGuide) {
        Button(
            onClick = onShowFilingSteps,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 8.dp, bottom = 12.dp)
        ) {
            Text("How to file it, step by step")
        }
    } else {
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun DocumentCard(item: CollectableDocument, motion: () -> Float, onClick: () -> Unit) {
    val collected = item.collected
    val wellColor = if (collected) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.surfaceContainerHigh
    }
    AppCard(
        onClick = onClick,
        color = if (collected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        outlined = !collected,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DocumentIllustration(
                art = item.document.art,
                containerColor = wellColor,
                motion = motion
            )
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp, end = 12.dp)) {
                Text(
                    text = item.document.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = if (collected) "Collected" else "Tap to tick it off",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                Checkbox(checked = collected, onCheckedChange = null)
            }
        }
        Text(
            text = item.document.why,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 14.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .clip(MaterialTheme.shapes.small)
                .background(wellColor)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            InlineArt(
                resource = Res.drawable.img_round_pushpin,
                size = 16.dp,
                modifier = Modifier.padding(top = 2.dp)
            )
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = "Where to get it",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.document.whereToGet,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}