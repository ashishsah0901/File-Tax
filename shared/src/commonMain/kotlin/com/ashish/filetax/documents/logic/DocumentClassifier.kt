package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind

internal object DocumentClassifier {

    fun classify(text: DocumentText): DocumentKind = when {
        text.contains("form no. 16a", "form no.16a", "certificate under section 203 for tax deducted at source on income other than salary") ->
            DocumentKind.FORM_16A

        text.contains("form no. 16b", "form no. 16c", "form no.16b", "form no.16c", "form 26qb", "form 26qc") ->
            DocumentKind.FORM_16B_OR_16C

        text.contains("form no. 12ba", "form 12ba", "statement showing particulars of perquisites") ->
            DocumentKind.FORM_12BA

        text.contains("form no. 12bb", "form 12bb") -> DocumentKind.FORM_12BB

        text.contains("form no. 27d", "form 27d", "certificate under section 206c") -> DocumentKind.FORM_27D

        text.contains("form no. 15g", "form no. 15h", "form 15g", "form 15h") -> DocumentKind.FORM_15G_OR_15H

        text.contains("form no. 10-iea", "form 10-iea", "form 10iea") -> DocumentKind.FORM_10IEA

        text.contains("form no. 10e", "form 10e", "relief under section 89", "relief u/s 89") -> DocumentKind.FORM_10E

        text.contains("form no. 16", "form no.16", "part b (annexure)", "certificate under section 203") ->
            DocumentKind.FORM_16

        text.contains("employee stock option", "stock option", "esop", "restricted stock unit") ->
            DocumentKind.ESOP_PERQUISITE_STATEMENT

        text.contains("full and final", "f&f settlement", "final settlement", "leave encashment", "relieving letter") ->
            DocumentKind.FULL_AND_FINAL_SETTLEMENT

        text.contains("annual information statement") -> DocumentKind.ANNUAL_INFORMATION_STATEMENT

        text.contains("taxpayer information summary") -> DocumentKind.TAXPAYER_INFORMATION_SUMMARY

        text.contains("form 26as", "form no. 26as", "annual tax statement") -> DocumentKind.FORM_26AS

        text.contains("indian income tax return acknowledgement", "itr-v", "acknowledgement number") ->
            DocumentKind.LAST_YEAR_RETURN

        text.contains("form no. 3cd", "form 3cd", "form no. 3cb", "form no. 3ca", "tax audit report", "section 44ab") ->
            DocumentKind.TAX_AUDIT_REPORT

        text.contains("challan", "itns 280", "counterfoil") -> DocumentKind.TAX_PAYMENT_CHALLAN

        text.contains("gstr", "goods and services tax", "gstin") -> DocumentKind.GST_RETURN

        text.contains("profit and loss", "profit & loss", "income and expenditure account") ->
            DocumentKind.PROFIT_AND_LOSS

        text.contains("balance sheet", "statement of assets and liabilities") -> DocumentKind.BALANCE_SHEET

        text.contains("turnover summary", "summary of turnover", "summary of receipts") -> DocumentKind.TURNOVER_SUMMARY

        text.contains("virtual digital asset", "crypto", "bitcoin", "ethereum", "nft") -> DocumentKind.CRYPTO_STATEMENT

        text.contains("intraday", "futures and options", "f&o", "speculative", "tradewise") ->
            DocumentKind.TRADING_PROFIT_AND_LOSS

        text.contains("cams", "kfintech", "karvy", "folio") -> DocumentKind.MUTUAL_FUND_STATEMENT

        text.contains("capital gain statement", "capital gains statement", "realised gain", "realized gain", "short term capital gain") ->
            DocumentKind.CAPITAL_GAINS_STATEMENT

        text.contains("partnership firm", "share of profit", "partner's capital account") ->
            DocumentKind.FIRM_SHARE_STATEMENT

        text.contains("winnings", "prize money", "lottery", "online game") -> DocumentKind.WINNINGS_CERTIFICATE

        text.contains("agricultural income", "land revenue receipt", "khasra", "mandi") ->
            DocumentKind.AGRICULTURAL_RECORDS

        text.contains("form 67", "tax residency certificate", "tax paid outside india", "withholding tax statement") ->
            DocumentKind.FOREIGN_TAX_PROOF

        text.contains("foreign bank account", "overseas account", "foreign asset", "foreign depository") ->
            DocumentKind.FOREIGN_ASSET_DETAILS

        text.contains("interest certificate", "certificate of interest", "interest paid certificate") ->
            DocumentKind.INTEREST_CERTIFICATE

        text.contains("housing loan", "home loan", "provisional certificate") -> DocumentKind.HOME_LOAN_CERTIFICATE

        text.contains("rent receipt", "rent agreement", "leave and licence", "lease deed", "rent received") ->
            DocumentKind.RENT_RECORDS

        text.contains("sale deed", "conveyance deed", "agreement to sell", "sale consideration") ->
            DocumentKind.PROPERTY_SALE_PAPERS

        text.contains("unlisted share", "share certificate") -> DocumentKind.UNLISTED_SHARE_DETAILS

        text.contains("director identification number", "directorship", "din:") -> DocumentKind.DIRECTOR_DETAILS

        text.contains("clubbing of income", "minor child", "income of spouse") -> DocumentKind.CLUBBED_INCOME_PROOF

        text.contains("representative assessee", "power of attorney", "legal heir") ->
            DocumentKind.REPRESENTATIVE_AUTHORITY

        text.contains("donation receipt", "premium receipt", "80g", "80c", "provident fund statement", "education loan") ->
            DocumentKind.DEDUCTION_PROOFS

        text.contains("statement of account", "account statement", "closing balance") ->
            DocumentKind.BUSINESS_BANK_STATEMENT

        text.contains("salary slip", "pay slip", "payslip", "earnings and deductions") -> DocumentKind.SALARY_SLIP

        text.contains("permanent account number card", "pan card", "e-pan") -> DocumentKind.PAN_CARD

        text.contains("cancelled cheque", "cheque leaf", "passbook", "payable at par", "ifsc") ->
            DocumentKind.BANK_ACCOUNT_PROOF

        text.contains("aadhaar", "uidai", "unique identification authority") -> DocumentKind.AADHAAR

        else -> DocumentKind.UNRECOGNISED
    }
}