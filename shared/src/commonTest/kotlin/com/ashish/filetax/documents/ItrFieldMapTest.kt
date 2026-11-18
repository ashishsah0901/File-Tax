package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.IndianAmountFormat
import com.ashish.filetax.documents.logic.ItrFieldMap
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.itr.ItrSchedule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItrFieldMapTest {

    @Test
    fun everyFigureKnowsWhereItGoes() {
        FigureKind.entries.forEach { kind ->
            val field = ItrFieldMap.forKind(kind)
            assertTrue(field.schedule in ItrSchedule.entries, "${kind.name} has an unknown schedule")
            assertTrue(field.field.isNotBlank(), "${kind.name} has no field")
            assertTrue(field.note.isNotBlank(), "${kind.name} has no note")
        }
    }

    @Test
    fun amountsReadTheIndianWay() {
        assertEquals("₹0", IndianAmountFormat.format(0))
        assertEquals("₹999", IndianAmountFormat.format(999))
        assertEquals("₹75,000", IndianAmountFormat.format(75000))
        assertEquals("₹18,45,000", IndianAmountFormat.format(1845000))
        assertEquals("₹1,23,45,678", IndianAmountFormat.format(12345678))
        assertEquals("-₹2,00,000", IndianAmountFormat.format(-200000))
    }
}