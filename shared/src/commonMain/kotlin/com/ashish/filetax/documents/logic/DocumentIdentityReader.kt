package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentIdentity

internal object DocumentIdentityReader {

    private val PAN = DetailPatterns.pan
    private val TAN = DetailPatterns.tan
    private val ASSESSMENT_YEAR = Regex("20[0-9]{2}\\s*-\\s*[0-9]{2,4}")

    private val OWNER_LABELS = arrayOf(
        "pan of the employee",
        "pan of the deductee",
        "pan of the taxpayer",
        "pan of taxpayer",
        "permanent account number"
    )

    fun read(
        text: DocumentText,
        issuerNeedles: List<String> = emptyList(),
        readOwnerPan: Boolean = true
    ): DocumentIdentity =
        DocumentIdentity(
            pan = if (readOwnerPan) ownerPan(text) else null,
            tan = text.match(TAN),
            assessmentYear = text.match(ASSESSMENT_YEAR)?.replace(" ", ""),
            issuer = issuer(text, issuerNeedles)
        )

    private fun ownerPan(text: DocumentText): String? {
        text.matchesOn(PAN, *OWNER_LABELS).lastOrNull()?.let { return it }
        val distinct = text.matches(PAN).distinct()
        return distinct.singleOrNull()
    }

    private fun issuer(text: DocumentText, needles: List<String>): String? {
        if (needles.isEmpty()) return null
        val index = text.indexOf(*needles.toTypedArray())
        if (index < 0) return null
        return text.lines.getOrNull(index + 1)?.takeIf { it.length in 3..80 }
    }
}