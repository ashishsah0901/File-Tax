package com.ashish.filetax.documents.logic

object IndianAmountFormat {

    fun format(amount: Long): String {
        val negative = amount < 0
        val digits = (if (negative) -amount else amount).toString()
        val grouped = if (digits.length <= 3) {
            digits
        } else {
            val last = digits.takeLast(3)
            val rest = digits.dropLast(3)
            buildString {
                var remaining = rest
                val parts = mutableListOf<String>()
                while (remaining.length > 2) {
                    parts.add(0, remaining.takeLast(2))
                    remaining = remaining.dropLast(2)
                }
                if (remaining.isNotEmpty()) parts.add(0, remaining)
                append(parts.joinToString(","))
                append(",")
                append(last)
            }
        }
        return if (negative) "-₹$grouped" else "₹$grouped"
    }
}