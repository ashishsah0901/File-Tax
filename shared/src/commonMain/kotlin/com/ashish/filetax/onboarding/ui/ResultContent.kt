package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.model.ItrRecommendation
import com.ashish.filetax.onboarding.model.TaxYear

@Composable
internal fun ColumnScope.ResultContent(
    recommendation: ItrRecommendation,
    onBack: () -> Unit,
    onShowDocuments: () -> Unit,
    onRestart: () -> Unit
) {
    Header(
        trailing = "AY ${TaxYear.ASSESSMENT_YEAR}",
        showBack = true,
        onBack = onBack
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "You need to file",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = recommendation.form.label,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = recommendation.form.summary,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Why this one",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        recommendation.reasons.forEach { reason ->
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 10.dp)
                )
                Text(
                    text = reason,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        recommendation.alternative?.let { alternative ->
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text(
                    text = alternative,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "Filing for FY ${TaxYear.FINANCIAL_YEAR}. Your answers stay on this phone. This is guidance built from the eligibility rules, not tax advice.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onShowDocuments,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("What I need to collect")
        }
        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change an answer")
        }
        TextButton(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start over")
        }
        Spacer(Modifier.height(16.dp))
    }
}