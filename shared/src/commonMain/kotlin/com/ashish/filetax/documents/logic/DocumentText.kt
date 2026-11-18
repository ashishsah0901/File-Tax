package com.ashish.filetax.documents.logic

internal class DocumentText(raw: String) {

    val lines: List<String> = raw.lines()
        .map { line -> line.replace('\u00A0', ' ').replace(WHITESPACE, " ").trim() }
        .filter { it.isNotEmpty() }

    private val lowered: List<String> = lines.map { it.lowercase() }

    fun contains(vararg needles: String): Boolean = indexOf(*needles) >= 0

    fun indexOf(vararg needles: String, from: Int = 0): Int {
        for (index in from until lowered.size) {
            if (needles.any { lowered[index].contains(it) }) return index
        }
        return -1
    }

    fun match(regex: Regex): String? = lines.firstNotNullOfOrNull { regex.find(it)?.value }

    fun matches(regex: Regex): List<String> = lines.flatMap { line -> regex.findAll(line).map { it.value } }

    fun matchesOn(regex: Regex, vararg needles: String): List<String> {
        var from = 0
        while (true) {
            val index = indexOf(*needles, from = from)
            if (index < 0) return emptyList()
            from = index + 1
            val found = regex.findAll(lines[index]).map { it.value }.toList()
            if (found.isNotEmpty()) return found
        }
    }

    fun amountFor(vararg needles: String, lookAhead: Int = 2, skip: List<String> = emptyList()): Amount? {
        var from = 0
        while (true) {
            val index = indexOf(*needles, from = from)
            if (index < 0) return null
            from = index + 1
            val line = lines[index]
            if (skip.any { line.lowercase().contains(it) }) continue
            Amounts.lastIn(line)?.let { return Amount(it, line) }
            for (offset in 1..lookAhead) {
                val next = lines.getOrNull(index + offset) ?: break
                Amounts.standaloneIn(next)?.let { return Amount(it, "$line $next") }
            }
        }
    }

    fun sumOf(vararg needles: String): Amount? {
        var from = 0
        var total = 0L
        var found = 0
        var firstLine: String? = null
        while (true) {
            val index = indexOf(*needles, from = from)
            if (index < 0) break
            from = index + 1
            val amount = Amounts.lastIn(lines[index]) ?: continue
            total += amount
            found += 1
            if (firstLine == null) firstLine = lines[index]
        }
        val line = firstLine ?: return null
        return Amount(total, if (found > 1) "$found entries, starting $line" else line)
    }

    private companion object {
        val WHITESPACE = Regex("\\s+")
    }
}

internal data class Amount(val value: Long, val sourceLine: String)