package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.filetax.documents.logic.DocumentFinding
import com.ashish.filetax.documents.logic.IndianAmountFormat
import com.ashish.filetax.onboarding.model.ItrRecommendation
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.tutorial.TutorialAnimation
import com.ashish.filetax.tutorial.ui.TutorialIllustration
import com.ashish.filetax.ui.components.AppCard
import com.ashish.filetax.ui.components.InlineArt
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_information
import filetax.shared.generated.resources.img_page_facing_up

@Composable
internal fun ColumnScope.ResultContent(
    recommendation: ItrRecommendation,
    documentFindings: List<DocumentFinding>,
    estimatedTotalIncome: Long?,
    onBack: () -> Unit,
    onShowDocuments: () -> Unit,
    onShowPrivacy: () -> Unit,
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
        TutorialIllustration(
            animation = TutorialAnimation.PICK_FORM,
            height = 140.dp,
            modifier = Modifier.padding(top = 12.dp)
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
            AppCard(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = alternative,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if (documentFindings.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "What your documents said",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            documentFindings.forEach { finding ->
                AppCard(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        InlineArt(
                            resource = Res.drawable.img_page_facing_up,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = finding.label,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = finding.evidence,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
            estimatedTotalIncome?.let { estimate ->
                Text(
                    text = "Those figures add up to roughly ${IndianAmountFormat.format(estimate)} of income. It is only what the documents you imported carry, so check it against the band you picked before you rely on it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        if (recommendation.cautions.isNotEmpty()) {
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Worth knowing",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(8.dp))
            recommendation.cautions.forEach { caution ->
                AppCard(modifier = Modifier.padding(bottom = 10.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        InlineArt(
                            resource = Res.drawable.img_information,
                            modifier = Modifier.padding(top = 1.dp)
                        )
                        Text(
                            text = caution,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
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
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)
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
        TextButton(
            onClick = onShowPrivacy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("How this app treats your data")
        }
        Spacer(Modifier.height(16.dp))
    }
}