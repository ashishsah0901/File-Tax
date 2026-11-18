package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedDetail
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object PanCardParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val details = buildList<ExtractedDetail> {
            add(text, DetailKind.PAN, DetailPatterns.pan)
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.PAN_CARD,
            identity = DocumentIdentityReader.read(text),
            figures = emptyList(),
            details = details
        )
    }
}

internal object AadhaarParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val details = buildList<ExtractedDetail> {
            add(text, DetailKind.AADHAAR_NUMBER, DetailPatterns.aadhaar)
            add(text, DetailKind.PAN, DetailPatterns.pan)
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.AADHAAR,
            identity = DocumentIdentityReader.read(text),
            figures = emptyList(),
            details = details
        )
    }
}

internal object BankAccountParser {

    fun parse(fileName: String, kind: DocumentKind, text: DocumentText): ParsedDocument {
        val details = buildList<ExtractedDetail> {
            addLabelled(
                text,
                DetailKind.BANK_ACCOUNT_NUMBER,
                DetailPatterns.accountNumber,
                "account number",
                "account no",
                "a/c no",
                "a/c number"
            )
            add(text, DetailKind.IFSC, DetailPatterns.ifsc)
            addValue(
                kind = DetailKind.BANK_NAME,
                value = text.lines.firstOrNull { it.lowercase().contains("bank") && it.length in 4..60 },
                sourceLine = text.lines.firstOrNull { it.lowercase().contains("bank") }.orEmpty()
            )
        }
        val figures = buildList<ExtractedFigure> {
            if (kind == DocumentKind.BUSINESS_BANK_STATEMENT) {
                add(text, FigureKind.CASH_BALANCE, "closing balance")
            }
        }
        return ParsedDocument(
            fileName = fileName,
            kind = kind,
            identity = DocumentIdentityReader.read(text),
            figures = figures,
            details = details
        )
    }
}

internal object TaxPaymentChallanParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val selfAssessment = text.contains("self assessment", "(300)", "300 self")
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                if (selfAssessment) FigureKind.SELF_ASSESSMENT_TAX else FigureKind.ADVANCE_TAX,
                "total amount",
                "amount (in rs",
                "amount in rs",
                "amount paid"
            )
        }
        val details = buildList<ExtractedDetail> {
            addLabelled(text, DetailKind.CHALLAN_BSR_CODE, DetailPatterns.bsrCode, "bsr code", "bsr")
            addLabelled(
                text,
                DetailKind.CHALLAN_SERIAL_NUMBER,
                DetailPatterns.challanSerial,
                "challan serial",
                "challan no",
                "challan number"
            )
            add(text, DetailKind.CHALLAN_DATE, DetailPatterns.date)
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.TAX_PAYMENT_CHALLAN,
            identity = DocumentIdentityReader.read(text),
            figures = figures,
            details = details
        )
    }
}

internal object TaxAuditReportParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.BUSINESS_RECEIPTS, "total turnover", "gross receipts", "turnover")
            add(text, FigureKind.BUSINESS_NET_PROFIT, "net profit")
        }
        val details = buildList<ExtractedDetail> {
            addLabelled(text, DetailKind.AUDIT_REPORT_UDIN, DetailPatterns.udin, "udin")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.TAX_AUDIT_REPORT,
            identity = DocumentIdentityReader.read(text),
            figures = figures,
            details = details
        )
    }
}

internal object Form10ieaParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val details = buildList<ExtractedDetail> {
            add(text, DetailKind.ACKNOWLEDGEMENT_NUMBER, DetailPatterns.acknowledgement)
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FORM_10IEA,
            identity = DocumentIdentityReader.read(text),
            figures = emptyList(),
            details = details
        )
    }
}