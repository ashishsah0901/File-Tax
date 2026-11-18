package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object Form12baParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.ESOP_PERQUISITE, "sweat equity", "stock option", "esop")
            add(
                text,
                FigureKind.PERQUISITES,
                "total value of perquisites",
                "total value of perquisite",
                "value of perquisites"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_12BA,
            identity = DocumentIdentityReader.read(text, listOf("name and address of the employer")),
            figures = figures
        )
    }
}

internal object FullAndFinalParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.LEAVE_ENCASHMENT, "leave encashment", "encashment of leave", "leave salary")
            add(text, FigureKind.GRATUITY, "gratuity")
            add(text, FigureKind.PENSION_COMMUTATION, "commuted pension", "commutation of pension")
            add(text, FigureKind.VRS_COMPENSATION, "voluntary retirement", "vrs compensation", "retrenchment compensation")
            add(text, FigureKind.NOTICE_PAY_RECOVERY, "notice pay", "notice period recovery")
            add(text, FigureKind.TDS_ON_SALARY, "tax deducted", "tds")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FULL_AND_FINAL_SETTLEMENT,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object Form10eParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.SALARY_ARREARS, "arrears or advance salary", "arrears of salary", "arrears")
            add(text, FigureKind.RELIEF_SECTION_89, "relief under section 89", "relief u/s 89", "relief under sec 89")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_10E,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object EsopStatementParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.ESOP_PERQUISITE, "perquisite value", "taxable perquisite", "perquisite")
            add(text, FigureKind.TDS_ON_SALARY, "tax deducted", "tds recovered")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.ESOP_PERQUISITE_STATEMENT,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object SalarySlipParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.GROSS_SALARY, "gross earnings", "total earnings", "gross salary", "gross pay")
            add(text, FigureKind.PROFESSIONAL_TAX, "professional tax", "p tax")
            add(text, FigureKind.TDS_ON_SALARY, "income tax", "tds")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.SALARY_SLIP,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object Form12bbParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.HOME_LOAN_INTEREST_PAID, "interest payable to the lender", "interest on borrowed capital")
            add(
                text,
                FigureKind.DEDUCTION_80C,
                skip = listOf("80ccc", "80ccd"),
                needles = arrayOf("section 80c", "life insurance", "provident fund")
            )
            add(text, FigureKind.DEDUCTION_80D, skip = listOf("80dd"), needles = arrayOf("section 80d", "health insurance"))
        }
        val details = buildList {
            addLabelled(
                text,
                DetailKind.LANDLORD_PAN,
                DetailPatterns.pan,
                "landlord",
                "permanent account number of the landlord"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_12BB,
            identity = DocumentIdentityReader.read(text, readOwnerPan = false),
            figures = figures,
            details = details
        )
    }
}