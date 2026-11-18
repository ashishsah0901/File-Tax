package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object AisParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.GROSS_SALARY, "salary received", "salary")
            add(text, FigureKind.SAVINGS_INTEREST, "interest from savings bank", "savings bank interest")
            add(text, FigureKind.DEPOSIT_INTEREST, "interest from deposit", "interest from term deposit")
            add(text, FigureKind.OTHER_INTEREST, "interest from others", "interest from income tax refund")
            add(text, FigureKind.DIVIDEND, "dividend")
            add(text, FigureKind.RENT_RECEIVED, "rent received", "rent on plant")
            add(text, FigureKind.SALE_OF_SECURITIES, "sale of securities and units of mutual fund", "sale of securities")
            add(text, FigureKind.SALE_OF_IMMOVABLE_PROPERTY, "sale of immovable property")
            add(text, FigureKind.PURCHASE_OF_IMMOVABLE_PROPERTY, "purchase of immovable property")
            add(text, FigureKind.BUSINESS_RECEIPTS, "business receipts", "receipts from business", "gst turnover")
            add(text, FigureKind.WINNINGS, "winnings from lottery", "winnings from online games", "winnings from horse race")
            add(text, FigureKind.FOREIGN_REMITTANCE, "outward foreign remittance")
            add(
                text,
                FigureKind.TDS_OTHER_THAN_SALARY,
                skip = listOf("salary", "tds/tcs information"),
                needles = arrayOf("tax deducted at source", "total tax deducted")
            )
            add(text, FigureKind.TCS, skip = listOf("tds/tcs information"), needles = arrayOf("tax collected at source"))
            add(text, FigureKind.ADVANCE_TAX, "advance tax")
            add(text, FigureKind.SELF_ASSESSMENT_TAX, "self assessment tax")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = kind,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}