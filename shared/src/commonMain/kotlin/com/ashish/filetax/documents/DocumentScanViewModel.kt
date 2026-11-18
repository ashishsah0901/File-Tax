package com.ashish.filetax.documents

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashish.filetax.documents.data.PdfExtraction
import com.ashish.filetax.documents.data.PdfTextExtractor
import com.ashish.filetax.documents.data.PickedDocument
import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.logic.ExtraDocuments
import com.ashish.filetax.documents.logic.ScanReportBuilder
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.documents.model.ScanReport
import com.ashish.filetax.onboarding.model.TaxDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class PasswordPrompt(val fileName: String, val wrongPassword: Boolean)

data class DocumentScanUiState(
    val whatYouNeed: List<TaxDocument>,
    val documents: List<ParsedDocument>,
    val report: ScanReport?,
    val readingFileName: String?,
    val passwordPrompt: PasswordPrompt?,
    val message: String?
)

class DocumentScanViewModel(
    private val extractor: PdfTextExtractor,
    private val checklist: List<TaxDocument>
) : ViewModel() {

    private var documents by mutableStateOf<List<ParsedDocument>>(emptyList())
    private var readingFileName by mutableStateOf<String?>(null)
    private var awaitingPassword by mutableStateOf<PickedDocument?>(null)
    private var wrongPassword by mutableStateOf(false)
    private var message by mutableStateOf<String?>(null)

    val uiState: DocumentScanUiState
        get() = DocumentScanUiState(
            whatYouNeed = ExtraDocuments.scannable(checklist),
            documents = documents,
            report = documents.takeIf { it.isNotEmpty() }?.let { ScanReportBuilder.build(it, checklist) },
            readingFileName = readingFileName,
            passwordPrompt = awaitingPassword?.let { PasswordPrompt(it.fileName, wrongPassword) },
            message = message
        )

    fun onDocumentPicked(picked: PickedDocument) = read(picked, null)

    fun onPasswordEntered(password: String) {
        val picked = awaitingPassword ?: return
        read(picked, password)
    }

    fun onPasswordDismissed() {
        awaitingPassword = null
        wrongPassword = false
    }

    fun onMessageShown() {
        message = null
    }

    fun onForgetDocuments() {
        documents = emptyList()
        awaitingPassword = null
        wrongPassword = false
        message = null
    }

    private fun read(picked: PickedDocument, password: String?) {
        readingFileName = picked.fileName
        viewModelScope.launch {
            when (val extraction = extractor.extract(picked.bytes, password)) {
                is PdfExtraction.Text -> onTextRead(picked, extraction.text)

                PdfExtraction.PasswordRequired -> {
                    awaitingPassword = picked
                    wrongPassword = false
                }

                PdfExtraction.WrongPassword -> {
                    awaitingPassword = picked
                    wrongPassword = true
                }

                is PdfExtraction.Failed -> {
                    awaitingPassword = null
                    message = extraction.reason
                }
            }
            readingFileName = null
        }
    }

    private suspend fun onTextRead(picked: PickedDocument, text: String) {
        val parsed = withContext(Dispatchers.Default) { DocumentScanner.parse(picked.fileName, text) }
        awaitingPassword = null
        wrongPassword = false
        documents = documents.filterNot { it.fileName == parsed.fileName } + parsed
        message = when {
            parsed.kind == DocumentKind.UNRECOGNISED ->
                "${picked.fileName} does not read like a tax document. Import the original PDF rather than a printout or a screenshot."

            parsed.figures.isEmpty() && parsed.details.isEmpty() ->
                "${picked.fileName} reads as ${parsed.kind.label}, but no figures could be picked out of it. Enter those numbers yourself."

            else -> null
        }
    }
}