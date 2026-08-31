package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.ashish.filetax.onboarding.OnboardingUiState
import com.ashish.filetax.onboarding.OnboardingViewModel
import com.ashish.filetax.onboarding.model.Choice
import com.ashish.filetax.onboarding.model.Question
import com.ashish.filetax.onboarding.model.TaxYear

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel, modifier: Modifier = Modifier) {
    val state = viewModel.uiState
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeContentPadding()
            .padding(horizontal = 20.dp)
    ) {
        when (state) {
            is OnboardingUiState.Asking -> AskingContent(
                state = state,
                onBack = viewModel::onBack,
                onChoiceSelected = viewModel::onChoiceSelected,
                onNoneOfTheseSelected = viewModel::onNoneOfTheseSelected,
                onContinue = viewModel::onContinue
            )

            is OnboardingUiState.Result -> ResultContent(
                recommendation = state.recommendation,
                onBack = viewModel::onBack,
                onShowDocuments = viewModel::onShowDocuments,
                onRestart = viewModel::onRestart
            )

            is OnboardingUiState.Documents -> DocumentsContent(
                state = state,
                onBack = viewModel::onBack,
                onDocumentToggled = viewModel::onDocumentToggled,
                onShowFilingSteps = viewModel::onShowFilingSteps
            )

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
    onContinue: () -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = state.canGoBack,
        onBack = onBack
    )
    LinearProgressIndicator(
        progress = { state.stepNumber.toFloat() / state.stepCount.toFloat() },
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
    )
    Text(
        text = "Question ${state.stepNumber} of ${state.stepCount}",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 12.dp)
    )
    Column(
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = state.question.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
        state.question.subtitle?.let { subtitle ->
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
    ) {
        Text(if (state.isLastStep) "See my form" else "Continue")
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
            MaterialTheme.colorScheme.surfaceVariant
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (multiSelect) {
                Checkbox(checked = selected, onCheckedChange = null)
            } else {
                RadioButton(selected = selected, onClick = null)
            }
            Column(modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 12.dp)) {
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