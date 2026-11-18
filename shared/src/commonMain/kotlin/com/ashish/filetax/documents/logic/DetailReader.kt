package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.ExtractedDetail

internal object DetailPatterns {
    val pan = Regex("[A-Z]{5}[0-9]{4}[A-Z]")
    val tan = Regex("[A-Z]{4}[0-9]{5}[A-Z]")
    val aadhaar = Regex("\\b[2-9][0-9]{3}\\s?[0-9]{4}\\s?[0-9]{4}\\b")
    val ifsc = Regex("\\b[A-Z]{4}0[A-Z0-9]{6}\\b")
    val gstin = Regex("\\b[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][0-9A-Z]Z[0-9A-Z]\\b")
    val acknowledgement = Regex("\\b[0-9]{15}\\b")
    val accountNumber = Regex("\\b[0-9]{9,18}\\b")
    val bsrCode = Regex("\\b[0-9]{7}\\b")
    val challanSerial = Regex("\\b[0-9]{5}\\b")
    val date = Regex("\\b[0-9]{2}[-/][0-9]{2}[-/][0-9]{4}\\b")
    val directorIdentificationNumber = Regex("\\b[0-9]{8}\\b")
    val udin = Regex("\\b[0-9]{8}[A-Z0-9]{10}\\b")
}

internal fun MutableList<ExtractedDetail>.add(text: DocumentText, kind: DetailKind, pattern: Regex) {
    if (any { it.kind == kind }) return
    val index = text.lines.indexOfFirst { pattern.containsMatchIn(it) }
    if (index < 0) return
    val value = pattern.find(text.lines[index])?.value ?: return
    add(ExtractedDetail(kind = kind, value = value, sourceLine = text.lines[index]))
}

internal fun MutableList<ExtractedDetail>.addLabelled(
    text: DocumentText,
    kind: DetailKind,
    pattern: Regex,
    vararg needles: String
) {
    if (any { it.kind == kind }) return
    val value = text.matchesOn(pattern, *needles).firstOrNull() ?: return
    val index = text.indexOf(*needles)
    add(ExtractedDetail(kind = kind, value = value, sourceLine = text.lines.getOrNull(index).orEmpty()))
}

internal fun MutableList<ExtractedDetail>.addValue(kind: DetailKind, value: String?, sourceLine: String) {
    if (value == null || any { it.kind == kind }) return
    add(ExtractedDetail(kind = kind, value = value, sourceLine = sourceLine))
}