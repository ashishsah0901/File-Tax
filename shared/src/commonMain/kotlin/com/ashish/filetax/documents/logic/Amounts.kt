package com.ashish.filetax.documents.logic

internal object Amounts {

    private val YEAR_RANGE = Regex("(19|20)\\d{2}\\s*[-–/]\\s*\\d{2,4}")
    private val CANDIDATE = Regex("\\d[\\d,]*(?:\\.\\d{1,2})?")
    private val COLUMN_WORDS = Regex(
        "\\b(gross|deductible|qualifying|eligible|amount|amt|total|rs|inr|value|paid|credited)\\b",
        RegexOption.IGNORE_CASE
    )
    private val PUNCTUATION = Regex("[₹:|()\\-.,\\s]")

    fun lastIn(line: String): Long? {
        val cleaned = line.replace(YEAR_RANGE, " ")
        val match = CANDIDATE.findAll(cleaned).lastOrNull { looksLikeMoney(it.value) } ?: return null
        val value = parse(match.value) ?: return null
        val before = cleaned.getOrNull(match.range.first - 1)
        return if (before == '-' || before == '(') -value else value
    }

    fun standaloneIn(line: String): Long? {
        val withoutColumns = line.replace(COLUMN_WORDS, " ")
        val remainder = CANDIDATE.replace(withoutColumns, " ").replace(PUNCTUATION, "")
        if (remainder.isNotEmpty()) return null
        return lastIn(withoutColumns)
    }

    fun parse(token: String): Long? {
        val digits = token.replace(",", "")
        val value = digits.toDoubleOrNull() ?: return null
        return if (value < 0) -((-value) + 0.5).toLong() else (value + 0.5).toLong()
    }

    private fun looksLikeMoney(token: String): Boolean =
        token.contains(',') || token.contains('.') || token.length >= 4
}