package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object CryptoStatementParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.VDA_SALE_CONSIDERATION,
                "total sell value",
                "total sale value",
                "sale consideration",
                "total sells"
            )
            add(
                text,
                FigureKind.VDA_COST_OF_ACQUISITION,
                "total buy value",
                "cost of acquisition",
                "total purchase value"
            )
            add(text, FigureKind.VDA_INCOME, "net gain", "total gain", "realised profit", "realized profit")
            add(text, FigureKind.TDS_OTHER_THAN_SALARY, "tds", "tax deducted")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.CRYPTO_STATEMENT,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object WinningsParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.WINNINGS, "gross winnings", "winnings", "prize money", "amount paid")
            add(text, FigureKind.TDS_OTHER_THAN_SALARY, "tax deducted", "tds")
        }
        val identity = DocumentIdentityReader.read(text, listOf("name and address of the deductor"))
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.WINNINGS_CERTIFICATE,
            identity = identity,
            figures = figures,
            details = buildList { addValue(DetailKind.DEDUCTOR_TAN, identity.tan, "TAN on the certificate") }
        )
    }
}

internal object AgriculturalRecordsParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.AGRICULTURAL_INCOME,
                "net agricultural income",
                "agricultural income",
                "income from agriculture"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.AGRICULTURAL_RECORDS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object ForeignTaxParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.FOREIGN_INCOME,
                "income from outside india",
                "foreign income",
                "gross income",
                "total income"
            )
            add(
                text,
                FigureKind.FOREIGN_TAX_PAID,
                "tax paid outside india",
                "foreign tax",
                "tax withheld",
                "withholding tax"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FOREIGN_TAX_PROOF,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object ForeignAssetParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.FOREIGN_ASSET_VALUE, "peak balance", "closing balance", "value of the asset")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FOREIGN_ASSET_DETAILS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object LastYearReturnParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.LAST_YEAR_TOTAL_INCOME, "total income")
            add(
                text,
                FigureKind.CARRIED_FORWARD_LOSS,
                "loss carried forward",
                "losses carried forward",
                "brought forward loss",
                "carried forward to future years"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.LAST_YEAR_RETURN,
            identity = DocumentIdentityReader.read(text),
            figures = figures,
            details = buildList { add(text, DetailKind.ACKNOWLEDGEMENT_NUMBER, DetailPatterns.acknowledgement) }
        )
    }
}

internal object UnlistedShareParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.UNLISTED_SHARE_COST, "cost of acquisition", "amount invested", "purchase cost")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.UNLISTED_SHARE_DETAILS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object DeductionProofsParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.DEDUCTION_80C,
                skip = listOf("80ccc", "80ccd"),
                needles = arrayOf("section 80c", "life insurance premium", "premium paid", "tuition fee", "provident fund")
            )
            add(text, FigureKind.DEDUCTION_80CCD1B, "80ccd(1b)", "national pension system", "nps contribution")
            add(text, FigureKind.DEDUCTION_80CCD2, "80ccd(2)", "employer contribution to nps")
            add(
                text,
                FigureKind.DEDUCTION_80D,
                skip = listOf("80dd"),
                needles = arrayOf("section 80d", "health insurance", "mediclaim")
            )
            add(text, FigureKind.DEDUCTION_80DDB, "80ddb", "treatment of specified disease")
            add(text, FigureKind.DEDUCTION_80E, "80e", "education loan interest", "interest on education loan")
            add(text, FigureKind.DEDUCTION_80G, "80g", "donation")
            add(text, FigureKind.DEDUCTION_80GG, "80gg", "rent paid")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.DEDUCTION_PROOFS,
            identity = DocumentIdentityReader.read(text, readOwnerPan = false),
            figures = figures,
            details = buildList { add(text, DetailKind.DONEE_PAN, DetailPatterns.pan) }
        )
    }
}

internal object DirectorDetailsParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument = ParsedDocument(
        fileName = fileName,
        kind = DocumentKind.DIRECTOR_DETAILS,
        identity = DocumentIdentityReader.read(text, readOwnerPan = false),
        figures = emptyList(),
        details = buildList {
            addLabelled(
                text,
                DetailKind.DIRECTOR_IDENTIFICATION_NUMBER,
                DetailPatterns.directorIdentificationNumber,
                "din",
                "director identification number"
            )
        }
    )
}