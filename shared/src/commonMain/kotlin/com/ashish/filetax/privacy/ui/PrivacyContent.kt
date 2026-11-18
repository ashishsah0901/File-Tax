package com.ashish.filetax.privacy.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.onboarding.ui.Header
import com.ashish.filetax.privacy.PrivacyPolicy
import com.ashish.filetax.tutorial.TutorialAnimation
import com.ashish.filetax.tutorial.ui.TutorialIllustration
import com.ashish.filetax.ui.components.AppCard
import com.ashish.filetax.ui.components.ArtTile
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_locked

@Composable
internal fun ColumnScope.PrivacyContent(onBack: () -> Unit) {
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
            text = "Privacy",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = PrivacyPolicy.HEADLINE,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )
        TutorialIllustration(
            animation = TutorialAnimation.PRIVACY,
            height = 150.dp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = TutorialAnimation.PRIVACY.caption,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
        )
        AppCard(
            color = MaterialTheme.colorScheme.primaryContainer,
            outlined = false,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ArtTile(
                    resource = Res.drawable.img_locked,
                    size = 56.dp,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
                Text(
                    text = PrivacyPolicy.SUMMARY,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).padding(start = 14.dp)
                )
            }
        }
        PrivacyPolicy.sections.forEach { section ->
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 24.dp)
            )
            section.body.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = PrivacyPolicy.LAST_UPDATED,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
    }
}