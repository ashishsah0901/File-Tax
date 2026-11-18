package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object Form16Parser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.GROSS_SALARY, "salary as per provisions contained in section 17(1)", "gross salary")
            add(text, FigureKind.PERQUISITES, "value of perquisites under section 17(2)", "perquisites u/s 17(2)")
            add(
                text,
                FigureKind.EXEMPT_ALLOWANCES,
                "total amount of exemption claimed under section 10",
                "allowance to the extent exempt under section 10",
                "allowances to the extent exempt under section 10"
            )
            add(text, FigureKind.STANDARD_DEDUCTION, "standard deduction under section 16(ia)", "standard deduction")
            add(text, FigureKind.PROFESSIONAL_TAX, "tax on employment under section 16(iii)", "professional tax")
            add(text, FigureKind.INCOME_CHARGEABLE_UNDER_SALARY, "income chargeable under the head")
            add(
                text,
                FigureKind.HOUSE_PROPERTY_INCOME,
                "income (or admissible loss) from house property",
                "loss from house property"
            )
            add(
                text,
                FigureKind.OTHER_INCOME_REPORTED_TO_EMPLOYER,
                "income under the head other sources offered for tds"
            )
            add(text, FigureKind.GROSS_TOTAL_INCOME, "gross total income")
            add(text, FigureKind.DEDUCTION_80C, skip = listOf("80ccc", "80ccd"), needles = arrayOf("section 80c", "80c "))
            add(text, FigureKind.DEDUCTION_80CCD1B, "80ccd(1b)", "80ccd (1b)")
            add(text, FigureKind.DEDUCTION_80D, skip = listOf("80dd"), needles = arrayOf("section 80d", "80d "))
            add(text, FigureKind.DEDUCTION_80TTA_TTB, "80tta", "80ttb")
            add(
                text,
                FigureKind.TOTAL_CHAPTER_VIA_DEDUCTIONS,
                "aggregate of deductible amount under chapter vi-a",
                "total deduction under chapter vi-a"
            )
            add(text, FigureKind.TOTAL_TAXABLE_INCOME, "total taxable income")
            add(
                text,
                FigureKind.TDS_ON_SALARY,
                skip = listOf("verification", "in words"),
                needles = arrayOf("total amount of tax deducted at source", "amount of tax deducted", "tax deducted at source u/s 192")
            )
        }
        val identity = DocumentIdentityReader.read(text, listOf("name and address of the employer"))
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_16,
            identity = identity,
            figures = figures,
            details = buildList {
                addValue(DetailKind.DEDUCTOR_TAN, identity.tan, "TAN on the certificate")
                addValue(DetailKind.PAN, identity.pan, "PAN on the certificate")
            }
        )
    }
}