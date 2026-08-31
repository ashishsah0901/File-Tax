package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.DocumentArt
import com.ashish.filetax.onboarding.model.TaxDocument
import com.ashish.filetax.onboarding.model.TaxYear

object DocumentCatalog {

    val pan = TaxDocument(
        id = "pan",
        name = "PAN card",
        why = "Your PAN is your login and the number the whole return is filed against.",
        whereToGet = "You already have it. Lost it? Download the e-PAN free from incometax.gov.in, under Our Services.",
        art = DocumentArt.IDENTITY_CARD
    )

    val aadhaar = TaxDocument(
        id = "aadhaar",
        name = "Aadhaar number",
        why = "The return is not complete until you verify it, and the quickest way is an OTP on the phone linked to your Aadhaar.",
        whereToGet = "Your Aadhaar card or the mAadhaar app. Check on incometax.gov.in that it is linked to your PAN.",
        art = DocumentArt.IDENTITY_CARD
    )

    val annualInformationStatement = TaxDocument(
        id = "ais",
        name = "Annual Information Statement (AIS)",
        why = "The tax department's own record of your year: salary, interest, dividends, shares you sold, big purchases. Start here and nothing gets left out.",
        whereToGet = "incometax.gov.in, log in, then Services, then AIS. Download the PDF for FY ${TaxYear.FINANCIAL_YEAR}.",
        art = DocumentArt.PORTAL
    )

    val form26as = TaxDocument(
        id = "form_26as",
        name = "Form 26AS",
        why = "Shows the tax already cut from your income and actually deposited against your PAN, which is what you claim back.",
        whereToGet = "incometax.gov.in, log in, e-File, Income Tax Returns, then View Form 26AS.",
        art = DocumentArt.PORTAL
    )

    val bankAccount = TaxDocument(
        id = "bank_account",
        name = "Bank account number and IFSC",
        why = "Any refund lands in this account, and it has to be pre-validated on the portal before that works.",
        whereToGet = "Your passbook, cheque book or bank app. Validate it under Profile, My Bank Accounts.",
        art = DocumentArt.PASSBOOK
    )

    val form16 = TaxDocument(
        id = "form_16",
        name = "Form 16",
        why = "Your employer's statement of what it paid you and what tax it cut. Most of the return is copied from this.",
        whereToGet = "Your employer or the payroll portal, usually by 15 June. Changed jobs? Get one from every employer.",
        art = DocumentArt.SHEET
    )

    val interestCertificate = TaxDocument(
        id = "interest_certificate",
        name = "Interest certificate from your bank",
        why = "Savings and fixed deposit interest is taxable even when no tax was cut, and this is the one number people forget.",
        whereToGet = "Net banking or your bank app, usually under Statements or Tax. Ask for the FY ${TaxYear.FINANCIAL_YEAR} interest certificate.",
        art = DocumentArt.PASSBOOK
    )

    val homeLoanCertificate = TaxDocument(
        id = "home_loan_certificate",
        name = "Home loan interest certificate",
        why = "Splits the year's payments into principal and interest, which are claimed in two different places.",
        whereToGet = "Your bank or housing finance company, from their app or a branch. Ask for the provisional or final interest certificate.",
        art = DocumentArt.HOUSE
    )

    val rentRecords = TaxDocument(
        id = "rent_records",
        name = "Rent received and property tax paid",
        why = "Rent is income, and the municipal tax you paid comes off it before tax is worked out.",
        whereToGet = "Your rent agreement and bank credits for the rent, plus the property tax receipt from your municipal corporation.",
        art = DocumentArt.RECEIPT
    )

    val brokerStatement = TaxDocument(
        id = "broker_statement",
        name = "Capital gains statement from your broker",
        why = "Every buy and sell with dates and costs, already worked out into short-term and long-term gains.",
        whereToGet = "Your broker's app or website, usually called Tax P&L or Capital Gains, for FY ${TaxYear.FINANCIAL_YEAR}.",
        art = DocumentArt.CHART
    )

    val mutualFundStatement = TaxDocument(
        id = "mutual_fund_statement",
        name = "Mutual fund capital gains statement",
        why = "Fund houses report gains differently from brokers, so units you redeemed need their own statement.",
        whereToGet = "A consolidated statement from CAMS or KFintech by email, or the capital gains report in your fund app.",
        art = DocumentArt.CHART
    )

    val propertySalePapers = TaxDocument(
        id = "property_sale_papers",
        name = "Sale and purchase deeds for the property",
        why = "The gain is the sale price minus what you paid and spent, and every figure has to come off a document.",
        whereToGet = "Your own file or the sub-registrar's office. Also keep improvement bills, brokerage receipts and the Form 16B the buyer gave you.",
        art = DocumentArt.HOUSE
    )

    val otherAssetProof = TaxDocument(
        id = "other_asset_proof",
        name = "Purchase and sale proof for what else you sold",
        why = "Gold, bonds, jewellery and unlisted shares have no ready statement, so the invoices are your only record of cost.",
        whereToGet = "The original invoice or purchase bill, and the sale receipt or contract note.",
        art = DocumentArt.RECEIPT
    )

    val cryptoStatement = TaxDocument(
        id = "crypto_statement",
        name = "Crypto transaction report",
        why = "Every trade is taxed at a flat rate with no set-off of losses, and 1% was already cut on each sale.",
        whereToGet = "Your exchange's app, usually under Reports or Tax, for FY ${TaxYear.FINANCIAL_YEAR}.",
        art = DocumentArt.COIN
    )

    val businessBankStatements = TaxDocument(
        id = "business_bank_statements",
        name = "Bank statements for the full year",
        why = "Your money in and money out for the year is what your income is worked back from.",
        whereToGet = "Net banking, for every account the business used, from 1 April to 31 March.",
        art = DocumentArt.PASSBOOK
    )

    val turnoverSummary = TaxDocument(
        id = "turnover_summary",
        name = "Total sales, and how much came in digitally",
        why = "The presumptive rate depends on this split, and a lower rate applies to what came in by UPI, card or transfer.",
        whereToGet = "Your invoices, UPI or card settlement reports, and your cash book.",
        art = DocumentArt.SHEET
    )

    val booksOfAccounts = TaxDocument(
        id = "books_of_accounts",
        name = "Profit and loss account, and balance sheet",
        why = "Declaring real profit means the return asks for the full accounts, line by line.",
        whereToGet = "Your accountant, or your accounting software's year-end reports.",
        art = DocumentArt.FOLDER
    )

    val gstReturns = TaxDocument(
        id = "gst_returns",
        name = "GST returns, if you are registered",
        why = "The turnover you declare here should match what you already told the GST department.",
        whereToGet = "gst.gov.in, log in, then Returns Dashboard. Download the GSTR-3B summary for the year.",
        art = DocumentArt.PORTAL
    )

    val firmShareStatement = TaxDocument(
        id = "firm_share_statement",
        name = "Your share statement from the firm",
        why = "Your profit share is exempt but the salary and interest the firm pays you are taxable, and the two have to be split.",
        whereToGet = "The firm's accountant. Keep the partnership deed and the firm's computation with it.",
        art = DocumentArt.SHEET
    )

    val form16a = TaxDocument(
        id = "form_16a",
        name = "Form 16A from anyone who paid you",
        why = "Clients cut tax on professional fees and commission, and this is the proof you claim it back with.",
        whereToGet = "Whoever paid you. Cross-check every one against your Form 26AS.",
        art = DocumentArt.SHEET
    )

    val winningsCertificate = TaxDocument(
        id = "winnings_certificate",
        name = "Winnings statement from the platform",
        why = "Winnings are taxed at a flat rate with tax already cut, and nothing can be set off against them.",
        whereToGet = "The gaming, betting or lottery platform's tax statement or TDS certificate.",
        art = DocumentArt.RECEIPT
    )

    val agriculturalRecords = TaxDocument(
        id = "agricultural_records",
        name = "Record of your farm income",
        why = "Farm income stays exempt, but above ₹5,000 it has to be reported with the land it came from.",
        whereToGet = "Your sale receipts from the mandi or buyer, and your land records.",
        art = DocumentArt.RECEIPT
    )

    val foreignAssetDetails = TaxDocument(
        id = "foreign_asset_details",
        name = "Statements for everything you hold abroad",
        why = "Schedule FA asks for each holding by calendar year with its peak value, and leaving one out is treated seriously.",
        whereToGet = "Your foreign broker or bank, your employer's RSU or ESPP portal, and any overseas property papers.",
        art = DocumentArt.GLOBE
    )

    val foreignTaxProof = TaxDocument(
        id = "foreign_tax_proof",
        name = "Proof of tax paid outside India",
        why = "You can set foreign tax off against Indian tax, but only with proof and only if Form 67 is filed first.",
        whereToGet = "The foreign payer's withholding statement or the tax receipt from that country.",
        art = DocumentArt.GLOBE
    )

    val lastYearReturn = TaxDocument(
        id = "last_year_return",
        name = "Last year's return and its computation",
        why = "A loss can only be carried forward if it was reported last year, and the figures have to carry over exactly.",
        whereToGet = "incometax.gov.in, log in, e-File, Income Tax Returns, View Filed Returns. Download the ITR and the acknowledgement.",
        art = DocumentArt.FOLDER
    )

    val unlistedShareDetails = TaxDocument(
        id = "unlisted_share_details",
        name = "Details of your unlisted shares",
        why = "The return asks for the company's name and PAN, how many shares you held and what you paid, opening and closing.",
        whereToGet = "Your holding or ESOP statement, and the company's secretary or founder for its PAN.",
        art = DocumentArt.SHEET
    )

    val esopPerquisiteStatement = TaxDocument(
        id = "esop_perquisite_statement",
        name = "ESOP perquisite statement",
        why = "Deferred ESOP tax has its own schedule, tracking what was deferred and when it falls due.",
        whereToGet = "Your employer's finance team, along with the Form 16 that shows the perquisite.",
        art = DocumentArt.SHEET
    )

    val directorDetails = TaxDocument(
        id = "director_details",
        name = "Your DIN and the company's details",
        why = "Directors have to list every company they are on the board of, with its name and PAN.",
        whereToGet = "Your DIN letter, or search the company on mca.gov.in.",
        art = DocumentArt.IDENTITY_CARD
    )

    val clubbedIncomeProof = TaxDocument(
        id = "clubbed_income_proof",
        name = "Statement for the income in another name",
        why = "Interest earned in a minor child's name is taxed in yours, so it needs its own certificate.",
        whereToGet = "The bank or fund house that holds the deposit or investment.",
        art = DocumentArt.PASSBOOK
    )

    val representativeAuthority = TaxDocument(
        id = "representative_authority",
        name = "Proof that you can file for them",
        why = "Filing for someone else needs your authority on record, plus their PAN and details.",
        whereToGet = "Your guardianship order, power of attorney, or the legal heir certificate.",
        art = DocumentArt.FOLDER
    )

    val deductionProofs = TaxDocument(
        id = "deduction_proofs",
        name = "Investment and insurance proofs",
        why = "Only needed if you file under the old regime, where these bring your taxable income down.",
        whereToGet = "PPF and ELSS statements, LIC and health insurance receipts, rent receipts for HRA, donation receipts, and the education loan interest certificate.",
        art = DocumentArt.RECEIPT
    )
}