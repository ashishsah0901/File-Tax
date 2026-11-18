package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.filetax.documents.ui.DocumentScanScreen
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.OnboardingViewModel
import com.ashish.filetax.onboarding.model.Choice
import com.ashish.filetax.onboarding.model.Question
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.privacy.ui.PrivacyContent
import com.ashish.filetax.ui.components.ArtTile

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, modifier: Modifier = Modifier) {
    val state = viewModel.uiState
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(horizontal = 16.dp)
    ) {
        when (state) {
            is OnboardingUiState.Asking -> AskingContent(
                state = state,
                onBack = viewModel::onBack,
                onChoiceSelected = viewModel::onChoiceSelected,
                onNoneOfTheseSelected = viewModel::onNoneOfTheseSelected,
                onContinue = viewModel::onContinue,
                onStartFromDocuments = viewModel::onShowScanner
            )

            is OnboardingUiState.Result -> ResultContent(
                recommendation = state.recommendation,
                documentFindings = state.documentFindings,
                estimatedTotalIncome = state.estimatedTotalIncome,
                onBack = viewModel::onBack,
                onShowDocuments = viewModel::onShowDocuments,
                onShowPrivacy = viewModel::onShowPrivacy,
                onRestart = viewModel::onRestart
            )

            is OnboardingUiState.Documents -> DocumentsContent(
                state = state,
                onBack = viewModel::onBack,
                onDocumentToggled = viewModel::onDocumentToggled,
                onShowFilingSteps = viewModel::onShowFilingSteps,
                onShowScanner = viewModel::onShowScanner
            )

            is OnboardingUiState.Scanning -> DocumentScanScreen(
                checklist = state.checklist,
                onBack = viewModel::onBack,
                onShowPrivacy = viewModel::onShowPrivacy,
                onUseDocuments = viewModel::onUseDocuments
            )

            is OnboardingUiState.Privacy -> PrivacyContent(onBack = viewModel::onBack)

            is OnboardingUiState.FilingSteps -> FilingStepsContent(
                state = state,
                onBack = viewModel::onBack,
                onStepToggled = viewModel::onStepToggled
            )
        }
    }
}

@Composable
private fun ColumnScope.AskingContent(
    state: OnboardingUiState.Asking,
    onBack: () -> Unit,
    onChoiceSelected: (String) -> Unit,
    onNoneOfTheseSelected: () -> Unit,
    onContinue: () -> Unit,
    onStartFromDocuments: () -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = state.canGoBack,
        onBack = onBack
    )
    LinearProgressIndicator(
        progress = { state.stepNumber.toFloat() / state.stepCount.toFloat() },
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp)
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Question ${state.stepNumber} of ${state.stepCount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.question.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            ArtTile(
                resource = state.question.id.image,
                size = 88.dp,
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        state.question.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        val multiple = state.question is Question.MultipleChoice
        state.question.choices.forEach { choice ->
            ChoiceRow(
                choice = choice,
                selected = choice.key in state.selection,
                multiSelect = multiple,
                onClick = { onChoiceSelected(choice.key) }
            )
        }
        (state.question as? Question.MultipleChoice)?.noneOfTheseLabel?.let { label ->
            ChoiceRow(
                choice = Choice(key = label, label = label),
                selected = state.noneSelected,
                multiSelect = true,
                onClick = onNoneOfTheseSelected
            )
        }
        Spacer(Modifier.height(16.dp))
    }
    Button(
        onClick = onContinue,
        enabled = state.canContinue,
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 12.dp)
    ) {
        Text(if (state.isLastStep) "See my form" else "Continue")
    }
    TextButton(
        onClick = onStartFromDocuments,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
    ) {
        Text("Read my documents and fill this in")
    }
}

@Composable
private fun ChoiceRow(
    choice: Choice,
    selected: Boolean,
    multiSelect: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 6.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (multiSelect) {
                Checkbox(checked = selected, onCheckedChange = null)
            } else {
                RadioButton(selected = selected, onClick = null)
            }
            Column(modifier = Modifier.padding(start = 6.dp, top = 10.dp, bottom = 10.dp)) {
                Text(
                    text = choice.label,
                    style = MaterialTheme.typography.bodyLarge
                )
                choice.hint?.let { hint ->
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
internal fun Header(trailing: String, showBack: Boolean, onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showBack) {
            TextButton(onClick = onBack, modifier = Modifier.padding(start = 0.dp)) {
                Text("Back")
            }
        } else {
            Spacer(Modifier.height(1.dp))
        }
        Text(
            text = trailing,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}