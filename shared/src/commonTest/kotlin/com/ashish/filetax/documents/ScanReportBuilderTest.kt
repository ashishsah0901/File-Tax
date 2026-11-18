package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.logic.ScanReportBuilder
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.onboarding.logic.DocumentChecklist
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScanReportBuilderTest {

    private val answers = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = setOf(IncomeSource.SALARY_PENSION, IncomeSource.INTEREST_DIVIDEND_OTHER)
    )

    @Test
    fun everyFigureIsGivenAPlaceInTheReturn() {
        val report = ScanReportBuilder.build(
            documents = listOf(DocumentScanner.parse("form16.pdf", DocumentSamples.form16)),
            checklist = DocumentChecklist.forAnswers(answers)
        )

        val entries = report.sections.flatMap { it.entries }
        assertTrue(entries.isNotEmpty())
        assertTrue(entries.all { it.field.field.isNotBlank() && it.field.note.isNotBlank() })
        assertEquals(
            entries.map { it.figure.kind }.distinct().size,
            entries.size
        )
    }

    @Test
    fun sectionsFollowTheOrderOfTheReturn() {
        val report = ScanReportBuilder.build(
            documents = listOf(
                DocumentScanner.parse("form16.pdf", DocumentSamples.form16),
                DocumentScanner.parse("ais.pdf", DocumentSamples.ais)
            ),
            checklist = DocumentChecklist.forAnswers(answers)
        )

        val positions = report.sections.map { ItrSchedule.entries.indexOf(it.schedule) }
        assertEquals(positions.sorted(), positions)
    }

    @Test
    fun theMoreAuthoritativeDocumentWinsAndTheRestBecomesACrossCheck() {
        val report = ScanReportBuilder.build(
            documents = listOf(
                DocumentScanner.parse("ais.pdf", DocumentSamples.ais),
                DocumentScanner.parse("form16.pdf", DocumentSamples.form16)
            ),
            checklist = DocumentChecklist.forAnswers(answers)
        )

        val salary = report.sections
            .flatMap { it.entries }
            .single { it.figure.kind == FigureKind.GROSS_SALARY }
        assertEquals(DocumentKind.FORM_16, salary.source)
        assertEquals(1845000L, salary.figure.amount)
        assertTrue(report.checks.any { it.title.startsWith("Gross salary: Form 16 against") })
    }

    @Test
    fun whatIsNotImportedYetIsListed() {
        val report = ScanReportBuilder.build(
            documents = listOf(DocumentScanner.parse("form16.pdf", DocumentSamples.form16)),
            checklist = DocumentChecklist.forAnswers(answers)
        )

        assertTrue(report.stillToScan.any { it.name.contains("Annual Information Statement") })
        assertTrue(report.stillToScan.none { it.name.contains("Form 16") })
    }
}