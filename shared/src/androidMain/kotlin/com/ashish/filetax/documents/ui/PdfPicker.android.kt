package com.ashish.filetax.documents.ui

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.ashish.filetax.documents.data.PickedDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
actual fun rememberPdfPicker(onPicked: (PickedDocument) -> Unit): () -> Unit {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val callback by rememberUpdatedState(onPicked)
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                val document = withContext(Dispatchers.IO) { context.readDocument(uri) }
                if (document != null) callback(document)
            }
        }
    }
    return { launcher.launch(arrayOf("application/pdf")) }
}

private fun Context.readDocument(uri: Uri): PickedDocument? {
    val bytes = runCatching {
        contentResolver.openInputStream(uri)?.use { it.readBytes() }
    }.getOrNull() ?: return null
    return PickedDocument(fileName = displayName(uri), bytes = bytes)
}

private fun Context.displayName(uri: Uri): String {
    val name = runCatching {
        contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }.getOrNull()
    return name ?: uri.lastPathSegment ?: "document.pdf"
}