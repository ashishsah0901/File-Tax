package com.ashish.filetax.privacy

import kotlin.test.Test
import kotlin.test.assertTrue

class PrivacyPolicyTest {

    @Test
    fun thePolicySaysTheOneThingItHasToSay() {
        val everything = (
            listOf(PrivacyPolicy.HEADLINE, PrivacyPolicy.SUMMARY) +
                PrivacyPolicy.sections.flatMap { listOf(it.title) + it.body }
            ).joinToString(" ").lowercase()

        assertTrue(everything.contains("leaves this phone"))
        assertTrue(everything.contains("no servers"))
        assertTrue(everything.contains("analytics"))
        assertTrue(everything.contains("on this device only"))
        assertTrue(everything.contains("uninstalling"))
    }

    @Test
    fun everySectionActuallySaysSomething() {
        assertTrue(PrivacyPolicy.sections.size >= 6)
        PrivacyPolicy.sections.forEach { section ->
            assertTrue(section.title.isNotBlank(), "a section has no title")
            assertTrue(section.body.isNotEmpty(), "${section.title} has no body")
            assertTrue(section.body.all { it.isNotBlank() }, "${section.title} has an empty paragraph")
        }
    }
}