package com.ashish.filetax.documents.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.filetax.documents.DocumentScanUiState
import com.ashish.filetax.documents.logic.DocumentPlacements
import com.ashish.filetax.documents.logic.ExtraDocuments
import com.ashish.filetax.documents.logic.IndianAmountFormat
import com.ashish.filetax.documents.model.CrossCheck
import com.ashish.filetax.documents.model.CrossCheckStatus
import com.ashish.filetax.documents.model.DetailEntry
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.documents.model.ReportEntry
import com.ashish.filetax.onboarding.model.DocumentArt
import com.ashish.filetax.onboarding.model.TaxDocument
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.onboarding.ui.DocumentIllustration
import com.ashish.filetax.onboarding.ui.Header
import com.ashish.filetax.tutorial.TutorialAnimation
import com.ashish.filetax.tutorial.ui.TutorialIllustration
import com.ashish.filetax.ui.components.AppCard
import com.ashish.filetax.ui.components.InlineArt
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_check_mark_button
import filetax.shared.generated.resources.img_cross_mark
import filetax.shared.generated.resources.img_information
import filetax.shared.generated.resources.img_warning

@Composable
internal fun ColumnScope.DocumentScanContent(
    state: DocumentScanUiState,
    onBack: () -> Unit,
    onShowPrivacy: () -> Unit,
    onUseDocuments: (List<ParsedDocument>) -> Unit,
    onPickPdf: () -> Unit,
    onPasswordEntered: (String) -> Unit,
    onPasswordDismissed: () -> Unit,
    onMessageShown: () -> Unit,
    onForgetDocuments: () -> Unit
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
            text = "Read my documents",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 12.dp)
        )
        Text(
            text = "Import the PDFs you have downloaded. They are opened and read on this phone alone, nothing is uploaded and nothing is kept once you leave.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        TextButton(
            onClick = onShowPrivacy,
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text("How this app treats your data")
        }
        state.message?.let { message ->
            MessageCard(message = message, onDismiss = onMessageShown)
        }
        if (state.documents.isEmpty()) {
            TutorialIllustration(
                animation = TutorialAnimation.SCAN,
                height = 150.dp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = TutorialAnimation.SCAN.caption,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        if (state.documents.isEmpty()) {
            WhatToImport(whatYouNeed = state.whatYouNeed)
        } else {
            ImportedDocuments(documents = state.documents, whatYouNeed = state.whatYouNeed)
            state.report?.let { report ->
                if (report.checks.isNotEmpty()) {
                    SectionTitle("What the documents say about each other")
                    report.checks.forEach { CrossCheckCard(it) }
                }
                report.sections.forEach { section ->
                    SectionTitle(section.schedule.label)
                    section.details.forEach { DetailCard(it) }
                    section.entries.forEach { EntryCard(it) }
                }
                if (report.stillToScan.isNotEmpty()) {
                    SectionTitle("Still to read")
                    report.stillToScan.forEach { document ->
                        DocumentSourceCard(
                            name = document.name,
                            whereToGet = document.whereToGet,
                            art = document.art
                        )
                    }
                }
            }
        }
        Text(
            text = "Every figure here is read off your own PDFs. Check each one against the document before you type it into the portal.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
        )
    }
    state.readingFileName?.let { fileName ->
        TutorialIllustration(animation = TutorialAnimation.SCAN, height = 72.dp)
        Text(
            text = "Reading $fileName",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 6.dp).height(6.dp))
    }
    if (state.documents.isNotEmpty()) {
        Button(
            onClick = { onUseDocuments(state.documents) },
            enabled = state.readingFileName == null,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 12.dp)
        ) {
            Text("Use these figures in my guide")
        }
        OutlinedButton(
            onClick = onPickPdf,
            enabled = state.readingFileName == null,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 8.dp)
        ) {
            Text("Add another PDF")
        }
    } else {
        Button(
            onClick = onPickPdf,
            enabled = state.readingFileName == null,
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp).padding(top = 12.dp)
        ) {
            Text("Pick a PDF")
        }
    }
    if (state.documents.isNotEmpty()) {
        TextButton(
            onClick = onForgetDocuments,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        ) {
            Text("Forget these documents")
        }
    } else {
        Spacer(Modifier.height(12.dp))
    }
    state.passwordPrompt?.let { prompt ->
        PasswordDialog(
            prompt = prompt,
            onSubmit = onPasswordEntered,
            onDismiss = onPasswordDismissed
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 12.dp, bottom = 10.dp)
    )
}

@Composable
private fun WhatToImport(whatYouNeed: List<TaxDocument>) {
    Text(
        text = "What your answers call for",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
    whatYouNeed.forEach { document ->
        DocumentSourceCard(
            name = document.name,
            whereToGet = document.whereToGet,
            art = document.art
        )
    }
    Text(
        text = "Other forms this app can read",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 8.dp, bottom = 10.dp)
    )
    ExtraDocuments.all.forEach { extra ->
        DocumentSourceCard(
            name = extra.kind.label,
            whereToGet = extra.whereToGet,
            art = DocumentArt.SHEET
        )
    }
    Text(
        text = "AIS and Form 16 open with a password: your PAN in lower case followed by your date of birth as DDMMYYYY.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun DocumentSourceCard(name: String, whereToGet: String, art: DocumentArt) {
    AppCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DocumentIllustration(art = art, size = 56.dp)
            Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = whereToGet,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ImportedDocuments(documents: List<ParsedDocument>, whatYouNeed: List<TaxDocument>) {
    SectionTitle("What you have imported")
    documents.forEach { document ->
        val art = whatYouNeed.firstOrNull { it.id == document.kind.checklistId }?.art ?: DocumentArt.SHEET
        AppCard(
            color = MaterialTheme.colorScheme.primaryContainer,
            outlined = false,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DocumentIllustration(
                    art = art,
                    size = 56.dp,
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                )
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(
                        text = document.kind.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = document.fileName,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
            Text(
                text = buildString {
                    val read = listOfNotNull(
                        document.figures.size.takeIf { it > 0 }
                            ?.let { if (it == 1) "1 figure read" else "$it figures read" },
                        document.details.size.takeIf { it > 0 }
                            ?.let { if (it == 1) "1 detail read" else "$it details read" }
                    )
                    append(if (read.isEmpty()) "Nothing could be read off it" else read.joinToString(" · "))
                    document.identity.issuer?.let { append(" · $it") }
                    document.identity.pan?.let { append(" · PAN $it") }
                    document.identity.assessmentYear?.let { append(" · AY $it") }
                },
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp)
            )
            Text(
                text = DocumentPlacements.forKind(document.kind),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun EntryCard(entry: ReportEntry) {
    AppCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entry.figure.kind.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = IndianAmountFormat.format(entry.figure.amount),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Text(
            text = "From ${entry.source.label}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = entry.field.field,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = entry.field.note,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun DetailCard(entry: DetailEntry) {
    AppCard(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = entry.detail.kind.label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = entry.detail.value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Text(
            text = "From ${entry.source.label}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            text = entry.field.field,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = entry.field.note,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun CrossCheckCard(check: CrossCheck) {
    AppCard(
        color = when (check.status) {
            CrossCheckStatus.MATCH -> MaterialTheme.colorScheme.primaryContainer
            CrossCheckStatus.MISMATCH -> MaterialTheme.colorScheme.errorContainer
            CrossCheckStatus.INFORMATION -> MaterialTheme.colorScheme.surfaceContainerLow
        },
        outlined = check.status == CrossCheckStatus.INFORMATION,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            InlineArt(
                resource = when (check.status) {
                    CrossCheckStatus.MATCH -> Res.drawable.img_check_mark_button
                    CrossCheckStatus.MISMATCH -> Res.drawable.img_cross_mark
                    CrossCheckStatus.INFORMATION -> Res.drawable.img_information
                },
                size = 22.dp,
                modifier = Modifier.padding(top = 1.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    text = check.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = check.detail,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun MessageCard(message: String, onDismiss: () -> Unit) {
    AppCard(
        color = MaterialTheme.colorScheme.errorContainer,
        outlined = false,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 6.dp),
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            InlineArt(
                resource = Res.drawable.img_warning,
                size = 22.dp,
                modifier = Modifier.padding(top = 1.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            )
        }
        TextButton(onClick = onDismiss, modifier = Modifier.padding(top = 4.dp)) {
            Text("Got it")
        }
    }
}