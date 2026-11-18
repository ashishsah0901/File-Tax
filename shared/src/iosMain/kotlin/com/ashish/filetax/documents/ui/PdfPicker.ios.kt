package com.ashish.filetax.documents.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.ashish.filetax.documents.data.PickedDocument
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberPdfPicker(onPicked: (PickedDocument) -> Unit): () -> Unit {
    val callback by rememberUpdatedState(onPicked)
    val delegate = remember { PdfPickerDelegate() }
    delegate.onPicked = { callback(it) }
    return {
        val controller = UIDocumentPickerViewController(forOpeningContentTypes = listOf(UTTypePDF))
        controller.allowsMultipleSelection = false
        controller.delegate = delegate
        UIApplication.sharedApplication.keyWindow?.rootViewController
            ?.presentViewController(controller, animated = true, completion = null)
    }
}

private class PdfPickerDelegate : NSObject(), UIDocumentPickerDelegateProtocol {

    var onPicked: ((PickedDocument) -> Unit)? = null

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: return
        val accessed = url.startAccessingSecurityScopedResource()
        val data = NSData.dataWithContentsOfURL(url)
        if (accessed) url.stopAccessingSecurityScopedResource()
        val bytes = data?.toByteArray() ?: return
        onPicked?.invoke(PickedDocument(fileName = url.lastPathComponent ?: "document.pdf", bytes = bytes))
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val result = ByteArray(size)
    result.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return result
}