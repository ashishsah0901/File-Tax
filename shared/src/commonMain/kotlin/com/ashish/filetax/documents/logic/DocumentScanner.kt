package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.ParsedDocument

object DocumentScanner {

    fun parse(fileName: String, rawText: String): ParsedDocument {
        val text = DocumentText(rawText)
        return when (val kind = DocumentClassifier.classify(text)) {
            DocumentKind.PAN_CARD -> PanCardParser.parse(fileName, text)
            DocumentKind.AADHAAR -> AadhaarParser.parse(fileName, text)
            DocumentKind.BANK_ACCOUNT_PROOF,
            DocumentKind.BUSINESS_BANK_STATEMENT -> BankAccountParser.parse(fileName, kind, text)

            DocumentKind.FORM_16 -> Form16Parser.parse(fileName, text)
            DocumentKind.FORM_12BA -> Form12baParser.parse(fileName, text)
            DocumentKind.FULL_AND_FINAL_SETTLEMENT -> FullAndFinalParser.parse(fileName, text)
            DocumentKind.FORM_10E -> Form10eParser.parse(fileName, text)
            DocumentKind.FORM_10IEA -> Form10ieaParser.parse(fileName, text)
            DocumentKind.ESOP_PERQUISITE_STATEMENT -> EsopStatementParser.parse(fileName, text)
            DocumentKind.FORM_16A,
            DocumentKind.FORM_16B_OR_16C -> TdsCertificateParser.parse(fileName, kind, text)

            DocumentKind.FORM_27D -> Form27dParser.parse(fileName, text)
            DocumentKind.SALARY_SLIP -> SalarySlipParser.parse(fileName, text)
            DocumentKind.FORM_12BB -> Form12bbParser.parse(fileName, text)
            DocumentKind.TRADING_PROFIT_AND_LOSS -> TradingProfitAndLossParser.parse(fileName, text)

            DocumentKind.INTEREST_CERTIFICATE -> InterestCertificateParser.parse(fileName, text)
            DocumentKind.HOME_LOAN_CERTIFICATE -> HomeLoanCertificateParser.parse(fileName, text)
            DocumentKind.RENT_RECORDS -> RentRecordsParser.parse(fileName, text)
            DocumentKind.PROPERTY_SALE_PAPERS,
            DocumentKind.OTHER_ASSET_PROOF -> PropertySaleParser.parse(fileName, kind, text)

            DocumentKind.CAPITAL_GAINS_STATEMENT,
            DocumentKind.MUTUAL_FUND_STATEMENT -> CapitalGainsStatementParser.parse(fileName, kind, text)

            DocumentKind.CRYPTO_STATEMENT -> CryptoStatementParser.parse(fileName, text)
            DocumentKind.PROFIT_AND_LOSS -> ProfitAndLossParser.parse(fileName, text)
            DocumentKind.BALANCE_SHEET -> BalanceSheetParser.parse(fileName, text)
            DocumentKind.TURNOVER_SUMMARY -> TurnoverSummaryParser.parse(fileName, text)
            DocumentKind.GST_RETURN -> GstReturnParser.parse(fileName, text)
            DocumentKind.TAX_AUDIT_REPORT -> TaxAuditReportParser.parse(fileName, text)
            DocumentKind.TAX_PAYMENT_CHALLAN -> TaxPaymentChallanParser.parse(fileName, text)
            DocumentKind.FIRM_SHARE_STATEMENT -> FirmShareParser.parse(fileName, text)
            DocumentKind.WINNINGS_CERTIFICATE -> WinningsParser.parse(fileName, text)
            DocumentKind.AGRICULTURAL_RECORDS -> AgriculturalRecordsParser.parse(fileName, text)
            DocumentKind.FOREIGN_TAX_PROOF -> ForeignTaxParser.parse(fileName, text)
            DocumentKind.FOREIGN_ASSET_DETAILS -> ForeignAssetParser.parse(fileName, text)
            DocumentKind.UNLISTED_SHARE_DETAILS -> UnlistedShareParser.parse(fileName, text)
            DocumentKind.DEDUCTION_PROOFS -> DeductionProofsParser.parse(fileName, text)
            DocumentKind.LAST_YEAR_RETURN -> LastYearReturnParser.parse(fileName, text)
            DocumentKind.FORM_26AS -> Form26asParser.parse(fileName, text)
            DocumentKind.ANNUAL_INFORMATION_STATEMENT,
            DocumentKind.TAXPAYER_INFORMATION_SUMMARY -> AisParser.parse(fileName, kind, text)

            DocumentKind.DIRECTOR_DETAILS -> DirectorDetailsParser.parse(fileName, text)
            DocumentKind.FORM_15G_OR_15H,
            DocumentKind.CLUBBED_INCOME_PROOF,
            DocumentKind.REPRESENTATIVE_AUTHORITY,
            DocumentKind.UNRECOGNISED -> PlainDocumentParser.parse(fileName, kind, text)
        }
    }
}