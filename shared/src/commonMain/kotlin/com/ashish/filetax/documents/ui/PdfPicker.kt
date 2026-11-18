package com.ashish.filetax.documents.ui

import androidx.compose.runtime.Composable
import com.ashish.filetax.documents.data.PickedDocument

@Composable
expect fun rememberPdfPicker(onPicked: (PickedDocument) -> Unit): () -> Unit