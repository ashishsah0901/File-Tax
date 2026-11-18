package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object RentRecordsParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.RENT_RECEIVED, "total rent received", "annual rent", "rent received", "rent for the year")
            add(text, FigureKind.MUNICIPAL_TAX_PAID, "municipal tax", "property tax", "house tax")
            add(text, FigureKind.TDS_OTHER_THAN_SALARY, "tax deducted", "tds")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.RENT_RECORDS,
            identity = DocumentIdentityReader.read(text, readOwnerPan = false),
            figures = figures,
            details = buildList { add(text, DetailKind.TENANT_PAN, DetailPatterns.pan) }
        )
    }
}

internal object PropertySaleParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.SALE_OF_IMMOVABLE_PROPERTY,
                "total sale consideration",
                "sale consideration",
                "total consideration"
            )
            add(text, FigureKind.STAMP_DUTY_VALUE, "stamp duty value", "circle rate", "guidance value")
            add(text, FigureKind.COST_OF_ACQUISITION, "cost of acquisition", "purchase price", "purchase consideration")
            add(text, FigureKind.TDS_OTHER_THAN_SALARY, "tax deducted", "tds deposited")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = kind,
            identity = DocumentIdentityReader.read(text, readOwnerPan = false),
            figures = figures,
            details = buildList { add(text, DetailKind.BUYER_PAN, DetailPatterns.pan) }
        )
    }
}

internal object TdsCertificateParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            addSum(text, FigureKind.TDS_OTHER_THAN_SALARY, "amount of tax deducted", "total tax deducted", "total (rs.)")
        }
        val identity = DocumentIdentityReader.read(text, listOf("name and address of the deductor"))
        return ParsedDocument(
            fileName = fileName,
            kind = kind,
            identity = identity,
            figures = figures,
            details = buildList { addValue(DetailKind.DEDUCTOR_TAN, identity.tan, "TAN on the certificate") }
        )
    }
}

internal object Form27dParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            addSum(text, FigureKind.TCS, "amount of tax collected", "total tax collected", "tax collected at source")
        }
        val identity = DocumentIdentityReader.read(text, listOf("name and address of the collector"))
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_27D,
            identity = identity,
            figures = figures,
            details = buildList { addValue(DetailKind.COLLECTOR_TAN, identity.tan, "TAN on the certificate") }
        )
    }
}