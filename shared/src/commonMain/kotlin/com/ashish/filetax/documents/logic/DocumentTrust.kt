package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind

internal object DocumentTrust {

    private val order = listOf(
        DocumentKind.PAN_CARD,
        DocumentKind.AADHAAR,
        DocumentKind.BANK_ACCOUNT_PROOF,
        DocumentKind.FORM_16,
        DocumentKind.FORM_12BA,
        DocumentKind.FULL_AND_FINAL_SETTLEMENT,
        DocumentKind.FORM_10E,
        DocumentKind.FORM_10IEA,
        DocumentKind.ESOP_PERQUISITE_STATEMENT,
        DocumentKind.SALARY_SLIP,
        DocumentKind.FORM_12BB,
        DocumentKind.FORM_16A,
        DocumentKind.FORM_27D,
        DocumentKind.FORM_15G_OR_15H,
        DocumentKind.FORM_16B_OR_16C,
        DocumentKind.INTEREST_CERTIFICATE,
        DocumentKind.HOME_LOAN_CERTIFICATE,
        DocumentKind.RENT_RECORDS,
        DocumentKind.PROPERTY_SALE_PAPERS,
        DocumentKind.OTHER_ASSET_PROOF,
        DocumentKind.CAPITAL_GAINS_STATEMENT,
        DocumentKind.TRADING_PROFIT_AND_LOSS,
        DocumentKind.MUTUAL_FUND_STATEMENT,
        DocumentKind.CRYPTO_STATEMENT,
        DocumentKind.TAX_AUDIT_REPORT,
        DocumentKind.PROFIT_AND_LOSS,
        DocumentKind.BALANCE_SHEET,
        DocumentKind.TURNOVER_SUMMARY,
        DocumentKind.GST_RETURN,
        DocumentKind.BUSINESS_BANK_STATEMENT,
        DocumentKind.FIRM_SHARE_STATEMENT,
        DocumentKind.WINNINGS_CERTIFICATE,
        DocumentKind.AGRICULTURAL_RECORDS,
        DocumentKind.FOREIGN_TAX_PROOF,
        DocumentKind.FOREIGN_ASSET_DETAILS,
        DocumentKind.UNLISTED_SHARE_DETAILS,
        DocumentKind.DIRECTOR_DETAILS,
        DocumentKind.CLUBBED_INCOME_PROOF,
        DocumentKind.REPRESENTATIVE_AUTHORITY,
        DocumentKind.DEDUCTION_PROOFS,
        DocumentKind.LAST_YEAR_RETURN,
        DocumentKind.TAX_PAYMENT_CHALLAN,
        DocumentKind.FORM_26AS,
        DocumentKind.ANNUAL_INFORMATION_STATEMENT,
        DocumentKind.TAXPAYER_INFORMATION_SUMMARY,
        DocumentKind.UNRECOGNISED
    )

    val taxpayerPanDocuments = setOf(
        DocumentKind.PAN_CARD,
        DocumentKind.AADHAAR,
        DocumentKind.FORM_16,
        DocumentKind.FORM_12BA,
        DocumentKind.SALARY_SLIP,
        DocumentKind.FORM_12BB,
        DocumentKind.FORM_16A,
        DocumentKind.FORM_27D,
        DocumentKind.FORM_15G_OR_15H,
        DocumentKind.ESOP_PERQUISITE_STATEMENT,
        DocumentKind.TAX_PAYMENT_CHALLAN,
        DocumentKind.ANNUAL_INFORMATION_STATEMENT,
        DocumentKind.TAXPAYER_INFORMATION_SUMMARY,
        DocumentKind.FORM_26AS,
        DocumentKind.LAST_YEAR_RETURN
    )

    fun rankOf(kind: DocumentKind): Int = order.indexOf(kind).takeIf { it >= 0 } ?: order.size

    internal fun ranked(): List<DocumentKind> = order
}