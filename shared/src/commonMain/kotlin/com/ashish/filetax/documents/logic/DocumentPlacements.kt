package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind

object DocumentPlacements {

    fun forKind(kind: DocumentKind): String = when (kind) {
        DocumentKind.PAN_CARD ->
            "Your PAN is the login and the number the return is filed against. It goes in Part A, and every document you claim credit for has to carry the same number."

        DocumentKind.AADHAAR ->
            "The Aadhaar number goes in Part A, and the OTP on the linked phone is the quickest way to verify the return. An unverified return counts as never filed."

        DocumentKind.BANK_ACCOUNT_PROOF ->
            "Account number, IFSC and bank name go into the bank account table. Pre-validate the account you want the refund in, and make sure the name on it matches your PAN."

        DocumentKind.FORM_10IEA ->
            "Filed on the portal before the return if you have business income and want the old regime. Part A then asks for its acknowledgement number and date."

        DocumentKind.TAX_AUDIT_REPORT ->
            "The auditor files it and you accept it on the portal before the return. The audit information block in Part A wants the auditor's details, the UDIN and the date."

        DocumentKind.TAX_PAYMENT_CHALLAN ->
            "Schedule IT, one row per challan: BSR code, serial number, date and amount, exactly as the counterfoil and Form 26AS show them."

        DocumentKind.FORM_16 ->
            "Part B is the return in miniature: salary into Schedule S, deductions into Schedule VI-A, and the TDS into Schedule TDS1 against your employer's TAN."

        DocumentKind.FORM_12BA ->
            "The itemised perquisites behind row 1(b) of Form 16. Nothing extra is added to the return, it only tells you what the perquisite figure is made of."

        DocumentKind.FULL_AND_FINAL_SETTLEMENT ->
            "Leave encashment, gratuity and severance all sit inside salary. The taxable part goes into Schedule S and the exempt part under the matching section 10 head."

        DocumentKind.FORM_10E ->
            "For arrears, this has to be filed on the portal as its own form before the return, and only then can relief under section 89 be claimed in Part B-TTI."

        DocumentKind.ESOP_PERQUISITE_STATEMENT ->
            "The perquisite on exercise is salary in the year you exercised. The shares themselves become a capital asset, so selling them later goes into Schedule CG, and a foreign parent's shares into Schedule FA."

        DocumentKind.SALARY_SLIP ->
            "Not entered anywhere by itself. March's slip is the quickest cross-check on Form 16: gross pay, professional tax and the tax cut through the year."

        DocumentKind.FORM_12BB ->
            "What you told your employer at the start of the year, which is why Form 16 looks the way it does. The landlord's PAN on it is what the HRA exemption rests on."

        DocumentKind.FORM_27D ->
            "Tax collected from you on a foreign remittance, a car or a sale of goods. It is claimed in Schedule TCS against the collector's TAN."

        DocumentKind.FORM_15G_OR_15H ->
            "A declaration that your income is below the taxable limit, so no TDS was cut. Nothing goes into the return from it, but the income it covers is still income and still has to be reported."

        DocumentKind.TRADING_PROFIT_AND_LOSS ->
            "Intraday goes into the speculative part of Schedule BP, futures and options into the normal part, and delivery trades into Schedule CG. This one document feeds all three."

        DocumentKind.FORM_16A ->
            "Each certificate is one row in Schedule TDS2: the deductor's TAN, the amount paid and the tax deducted. The income itself still has to be entered under its own head."

        DocumentKind.FORM_16B_OR_16C ->
            "Tax the buyer or tenant cut on your property or rent. Claim it in Schedule TDS2 and report the sale or the rent under Schedule CG or Schedule HP."

        DocumentKind.INTEREST_CERTIFICATE ->
            "Savings and deposit interest go into Schedule OS, and the TDS on it into Schedule TDS2. Under the old regime savings interest also earns a deduction under 80TTA or 80TTB."

        DocumentKind.HOME_LOAN_CERTIFICATE ->
            "Interest goes into Schedule HP against the property, principal into section 80C in Schedule VI-A. Both are old regime only."

        DocumentKind.RENT_RECORDS ->
            "Gross rent, municipal taxes and the tenant's PAN go into Schedule HP, one property at a time."

        DocumentKind.PROPERTY_SALE_PAPERS ->
            "Schedule CG: sale value, stamp duty value, cost, improvement and transfer expenses, the buyer's details, and any exemption under section 54, 54F or 54EC."

        DocumentKind.OTHER_ASSET_PROOF ->
            "Gold, unlisted shares, a plot or anything else sold goes into the other assets part of Schedule CG, with cost and date of purchase."

        DocumentKind.CAPITAL_GAINS_STATEMENT ->
            "Short term gains under 111A and long term under 112A in Schedule CG, with the scrip-wise detail in Schedule 112A."

        DocumentKind.MUTUAL_FUND_STATEMENT ->
            "Equity funds follow 111A and 112A. Debt funds bought on or after 1 April 2023 are short term whatever the holding period, so they go into the normal rate part of Schedule CG."

        DocumentKind.CRYPTO_STATEMENT ->
            "Schedule VDA, transfer by transfer, taxed at a flat 30 percent with no set-off. The 1 percent TDS under 194S is claimed in Schedule TDS2."

        DocumentKind.PROFIT_AND_LOSS ->
            "Part A-P&L line by line, and the net profit carries into Schedule BP where the disallowances are added back."

        DocumentKind.BALANCE_SHEET ->
            "Part A-BS as on 31 March. Presumptive filers still have to give debtors, creditors, stock and cash."

        DocumentKind.TURNOVER_SUMMARY ->
            "Split the turnover into what came through banking channels and what came in cash, because 44AD taxes them at 6 and 8 percent."

        DocumentKind.GST_RETURN ->
            "The GSTIN and the turnover reported under GST are asked for directly in the business schedules, and they should tie to your books."

        DocumentKind.BUSINESS_BANK_STATEMENT ->
            "Not entered anywhere by itself. It is what you build receipts, expenses and the closing bank balance from, and what the department asks for when something does not add up."

        DocumentKind.FIRM_SHARE_STATEMENT ->
            "The firm's details go into Schedule IF, your share of profit into Schedule EI as exempt, and remuneration and interest on capital into Schedule BP. This needs ITR-3."

        DocumentKind.WINNINGS_CERTIFICATE ->
            "The gross winning goes into the special rate part of Schedule OS at 30 percent, and the TDS into Schedule TDS2."

        DocumentKind.AGRICULTURAL_RECORDS ->
            "Schedule EI. Above ₹5,00,000 the schedule also wants the land's district, area, ownership and whether it is irrigated."

        DocumentKind.FOREIGN_TAX_PROOF ->
            "The income goes into Schedule FSI country by country and the credit into Schedule TR, but only after Form 67 has been filed on the portal."

        DocumentKind.FOREIGN_ASSET_DETAILS ->
            "Schedule FA, for every account, share, ESOP or property held at any time in the calendar year. It rules out ITR-1 and ITR-4."

        DocumentKind.UNLISTED_SHARE_DETAILS ->
            "The unlisted shares table wants the company's name and PAN, opening and closing holding, and anything bought or sold in the year."

        DocumentKind.DIRECTOR_DETAILS ->
            "The directorship table in Part A wants the company's name, PAN, DIN and whether its shares are listed. Being a director rules out ITR-1 and ITR-4."

        DocumentKind.CLUBBED_INCOME_PROOF ->
            "Income of a spouse or minor that the law treats as yours goes into Schedule SPI and then under its own head, with ₹1,500 exempt per minor."

        DocumentKind.REPRESENTATIVE_AUTHORITY ->
            "Filing for someone else is set up in the portal profile, not in a schedule. Keep the authority with the return."

        DocumentKind.DEDUCTION_PROOFS ->
            "Schedule VI-A, one section at a time. Nothing is uploaded with the return, but you need the proof if the deduction is questioned."

        DocumentKind.LAST_YEAR_RETURN ->
            "Nothing from it is entered as income. It carries the losses into Schedule CFL and the opening balances into the balance sheet."

        DocumentKind.FORM_26AS ->
            "The department's own record of tax against your PAN. Schedules TDS1, TDS2, TCS and IT should end up matching it exactly."

        DocumentKind.ANNUAL_INFORMATION_STATEMENT,
        DocumentKind.TAXPAYER_INFORMATION_SUMMARY ->
            "Not a schedule of its own. It is the checklist the department will compare your return against, head by head."

        DocumentKind.UNRECOGNISED ->
            "This one could not be placed. Import the original PDF from the issuer rather than a printout or a screenshot."
    }
}