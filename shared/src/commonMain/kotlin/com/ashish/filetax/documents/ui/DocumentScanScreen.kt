package com.ashish.filetax.documents.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ashish.filetax.documents.DocumentScanViewModel
import com.ashish.filetax.documents.data.pdfTextExtractor
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.onboarding.model.TaxDocument

@Composable
internal fun ColumnScope.DocumentScanScreen(
    checklist: List<TaxDocument>,
    onBack: () -> Unit,
    onShowPrivacy: () -> Unit,
    onUseDocuments: (List<ParsedDocument>) -> Unit
) {
    val viewModel = viewModel(key = "document_scan") {
        DocumentScanViewModel(pdfTextExtractor(), checklist)
    }
    val pickPdf = rememberPdfPicker(viewModel::onDocumentPicked)
    DocumentScanContent(
        state = viewModel.uiState,
        onBack = onBack,
        onShowPrivacy = onShowPrivacy,
        onUseDocuments = onUseDocuments,
        onPickPdf = pickPdf,
        onPasswordEntered = viewModel::onPasswordEntered,
        onPasswordDismissed = viewModel::onPasswordDismissed,
        onMessageShown = viewModel::onMessageShown,
        onForgetDocuments = viewModel::onForgetDocuments
    )
}