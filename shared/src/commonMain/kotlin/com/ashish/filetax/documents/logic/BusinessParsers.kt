package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ExtractedFigure
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument

internal object ProfitAndLossParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.BUSINESS_RECEIPTS,
                "revenue from operations",
                "gross receipts",
                "total turnover",
                "sales",
                "turnover"
            )
            add(text, FigureKind.BUSINESS_GROSS_PROFIT, "gross profit")
            add(text, FigureKind.DEPRECIATION, "depreciation")
            add(text, FigureKind.BUSINESS_EXPENSES, "total expenses", "total expenditure")
            add(
                text,
                FigureKind.BUSINESS_NET_PROFIT,
                skip = listOf("before tax"),
                needles = arrayOf("net profit", "profit for the year", "profit after tax")
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.PROFIT_AND_LOSS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object BalanceSheetParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.SUNDRY_DEBTORS, "sundry debtors", "trade receivables")
            add(text, FigureKind.SUNDRY_CREDITORS, "sundry creditors", "trade payables")
            add(text, FigureKind.CLOSING_STOCK, "closing stock", "inventories", "stock in trade")
            add(text, FigureKind.CASH_BALANCE, "cash and bank", "cash and cash equivalents", "cash in hand")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.BALANCE_SHEET,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object TurnoverSummaryParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.BUSINESS_RECEIPTS,
                "total turnover",
                "gross turnover",
                "total receipts",
                "gross receipts"
            )
            add(
                text,
                FigureKind.DIGITAL_TURNOVER,
                "through banking channels",
                "digital receipts",
                "non-cash receipts",
                "bank receipts"
            )
            add(text, FigureKind.CASH_TURNOVER, "cash receipts", "received in cash", "cash turnover")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.TURNOVER_SUMMARY,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}

internal object GstReturnParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(
                text,
                FigureKind.GST_TURNOVER,
                "aggregate turnover",
                "total turnover",
                "outward taxable supplies",
                "total value of supplies"
            )
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.GST_RETURN,
            identity = DocumentIdentityReader.read(text),
            figures = figures,
            details = buildList { add(text, DetailKind.GSTIN, DetailPatterns.gstin) }
        )
    }
}

internal object FirmShareParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.PARTNER_SHARE_OF_PROFIT, "share of profit", "share in profit")
            add(text, FigureKind.PARTNER_REMUNERATION, "remuneration", "salary to partner")
            add(text, FigureKind.PARTNER_INTEREST_ON_CAPITAL, "interest on capital")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.FIRM_SHARE_STATEMENT,
            identity = DocumentIdentityReader.read(text, readOwnerPan = false),
            figures = figures,
            details = buildList { add(text, DetailKind.FIRM_PAN, DetailPatterns.pan) }
        )
    }
}

internal object TradingProfitAndLossParser {

    fun parse(fileName: String, text: DocumentText): ParsedDocument {
        val figures = buildList<ExtractedFigure> {
            add(text, FigureKind.SPECULATIVE_BUSINESS_INCOME, "intraday", "speculative")
            add(text, FigureKind.DERIVATIVES_INCOME, "futures and options", "f&o", "derivative")
            add(text, FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY, "short term capital gain")
            add(text, FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY, "long term capital gain")
            add(text, FigureKind.BUSINESS_RECEIPTS, "total turnover", "trading turnover")
        }
        return ParsedDocument(
            fileName = fileName,
            kind = DocumentKind.TRADING_PROFIT_AND_LOSS,
            identity = DocumentIdentityReader.read(text),
            figures = figures
        )
    }
}