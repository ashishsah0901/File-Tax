package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object CapitalGainsStatementParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY,
                "total short term capital gain",
                "short term capital gain"
            )
            add(
                text,
                FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY,
                "total long term capital gain",
                "long term capital gain"
            )
            add(text, FigureKind.SALE_OF_SECURITIES, "total sale consideration", "sale consideration", "sale value")
            add(text, FigureKind.COST_OF_ACQUISITION, "cost of acquisition", "total purchase value")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = kind,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object InterestCertificateParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.SAVINGS_INTEREST, "interest on savings", "savings bank interest", "interest credited on savings")
            add(text, FigureKind.DEPOSIT_INTEREST, "interest on deposit", "interest on fixed deposit", "term deposit interest")
            add(text, FigureKind.TDS_OTHER_THAN_SALARY, "tax deducted", "tds deducted")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.INTEREST_CERTIFICATE,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object HomeLoanCertificateParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.HOME_LOAN_INTEREST_PAID, "interest", "interest component")
            add(text, FigureKind.HOME_LOAN_PRINCIPAL_PAID, "principal", "principal component")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.HOME_LOAN_CERTIFICATE,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object PlainDocumentParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument = ParsedDocument(
        fileName = fileName,
        kind = kind,
        identity = DocumentIdentityReader.read(text),
        figures = emptyList()
    )
}