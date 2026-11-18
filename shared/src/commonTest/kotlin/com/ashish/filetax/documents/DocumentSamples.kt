package com.ashish.filetax.documents

internal object DocumentSamples {

    val form16 = """
        FORM NO. 16
        [See rule 31(1)(a)]
        Certificate under Section 203 of the Income-tax Act, 1961 for tax deducted at source on salary
        Name and address of the Employer
        Acme Software Private Limited, Bengaluru
        PAN of the Employee: ABCDE1234F
        TAN of the Deductor: BLRA12345B
        Assessment Year 2026-27
        PART B (Annexure)
        1. Gross Salary
        (a) Salary as per provisions contained in section 17(1) 1845000.00
        (b) Value of perquisites under section 17(2) 62000.00
        (d) Total 1907000.00
        2. Less: Allowances to the extent exempt under section 10
        Total amount of exemption claimed under section 10 240000.00
        4. Less: Deductions under section 16
        (a) Standard deduction under section 16(ia) 75000.00
        (c) Tax on employment under section 16(iii) 2400.00
        5. Income chargeable under the head 'Salaries' 1589600.00
        6. Add: Any other income reported by the employee
        Income under the head other sources offered for TDS 18000.00
        8. Gross total income 1607600.00
        9. Deductions under Chapter VI-A
        (a) Deduction in respect of life insurance premia under section 80C
        Gross Amount 162000.00 Deductible Amount 150000.00
        (e) Deduction in respect of contribution to pension scheme under section 80CCD(1B) 50000.00
        (f) Deduction in respect of health insurance premia under section 80D 25000.00
        10. Aggregate of deductible amount under Chapter VI-A 225000.00
        11. Total taxable income 1382600.00
        12. Tax on total income 210780.00
        17. Amount of tax deducted 219210.00
    """.trimIndent()

    val ais = """
        Annual Information Statement
        Assessment Year 2026-27
        Permanent Account Number (PAN) ABCDE1234F
        Part B - Information
        Salary 1907000.00
        Interest from savings bank 18500.00
        Interest from deposit 64200.00
        Dividend 12800.00
        Sale of securities and units of mutual fund 480000.00
        Purchase of immovable property 3500000.00
        Outward foreign remittance 250000.00
        Tax deducted at source 21400.00
    """.trimIndent()

    val form26as = """
        Form 26AS
        Annual Tax Statement under Section 203AA
        Permanent Account Number (PAN): ABCDE1234F Assessment Year: 2026-27
        PART I - Details of Tax Deducted at Source
        Sr. No. Name of Deductor TAN of Deductor Section Total Amount Paid Total Tax Deducted Total TDS Deposited
        1 Acme Software Private Limited BLRA12345B 192 1907000.00 219210.00 219210.00
        PART II - Details of Tax Deducted at Source on interest
        2 State Bank of India BLRS54321C 194A 64200.00 6420.00 6420.00
        PART C - Details of Tax Paid (Other than TDS or TCS)
        Advance Tax 25000.00
        Self Assessment Tax 12000.00
    """.trimIndent()

    val capitalGains = """
        Capital Gains Statement for FY 2025-26
        Zerodha Broking Limited
        Total Short Term Capital Gain 48000.00
        Total Long Term Capital Gain 172000.00
        Total Sale Consideration 480000.00
    """.trimIndent()

    val bankInterestCertificate = """
        Interest Certificate for the Financial Year 2025-26
        State Bank of India
        Interest on savings account 18500.00
        Interest on fixed deposit 64200.00
        Tax deducted 6420.00
    """.trimIndent()

    val fullAndFinalSettlement = """
        Full and Final Settlement Statement
        Acme Software Private Limited
        PAN of the Employee: ABCDE1234F
        Leave encashment 268000.00
        Gratuity 480000.00
        Notice pay recovery 90000.00
        Tax deducted 62000.00
    """.trimIndent()

    val form12ba = """
        FORM NO. 12BA
        Statement showing particulars of perquisites, other fringe benefits and profits in lieu of salary
        Name and address of the Employer
        Acme Software Private Limited
        Stock options allotted or transferred by the employer 412000.00
        Total value of perquisites 474000.00
    """.trimIndent()

    val form10e = """
        FORM NO. 10E
        Form for furnishing particulars of income for claiming relief under section 89(1)
        Arrears of salary received 340000.00
        Relief under section 89 28500.00
    """.trimIndent()

    val esopStatement = """
        ESOP exercise statement
        Acme Software Private Limited
        Perquisite value 412000.00
        Tax deducted 128000.00
    """.trimIndent()

    val rentRecords = """
        Rent Receipt for the Financial Year 2025-26
        Total rent received 480000.00
        Municipal tax paid 12000.00
        Tax deducted 9600.00
    """.trimIndent()

    val propertySale = """
        Sale Deed
        Sale consideration 9500000.00
        Stamp duty value 9800000.00
        Purchase price 4200000.00
        Tax deducted 95000.00
    """.trimIndent()

    val form16b = """
        FORM NO. 16B
        Certificate under section 203 of the Income-tax Act, 1961 for tax deducted at source
        Amount of tax deducted 95000.00
    """.trimIndent()

    val profitAndLoss = """
        Profit and Loss Account for the year ended 31 March 2026
        Revenue from operations 8450000.00
        Cost of materials consumed 5100000.00
        Gross profit 3350000.00
        Depreciation 210000.00
        Total expenses 1980000.00
        Net profit 1370000.00
    """.trimIndent()

    val balanceSheet = """
        Balance Sheet as on 31 March 2026
        Sundry debtors 620000.00
        Closing stock 480000.00
        Sundry creditors 350000.00
        Cash and bank balance 275000.00
    """.trimIndent()

    val turnoverSummary = """
        Summary of turnover for FY 2025-26
        Total turnover 4800000.00
        Received through banking channels 4200000.00
        Cash receipts 600000.00
    """.trimIndent()

    val gstReturn = """
        GSTR-3B for the period April 2025 to March 2026
        GSTIN 29ABCDE1234F1Z5
        Total turnover 4500000.00
    """.trimIndent()

    val firmShare = """
        Statement of share from a partnership firm
        Nair and Associates
        Share of profit 850000.00
        Remuneration 1200000.00
        Interest on capital 180000.00
    """.trimIndent()

    val cryptoStatement = """
        Virtual Digital Asset transaction statement FY 2025-26
        Total buy value 620000.00
        Total sell value 540000.00
        TDS deducted under section 194S 5400.00
    """.trimIndent()

    val mutualFundStatement = """
        CAMS capital gains statement FY 2025-26
        Folio 1234567
        Total short term capital gain 32000.00
        Total long term capital gain 96000.00
        Total sale consideration 415000.00
    """.trimIndent()

    val deductionProofs = """
        Premium receipt for the Financial Year 2025-26
        Life insurance premium paid 48000.00
        Health insurance premium 26000.00
        Donation under section 80G 15000.00
        Interest on education loan 62000.00
    """.trimIndent()

    val lastYearReturn = """
        Indian Income Tax Return Acknowledgement
        Assessment Year 2025-26
        Total income 1245000.00
        Loss carried forward to future years 185000.00
    """.trimIndent()

    val agriculturalRecords = """
        Agricultural income statement FY 2025-26
        Net agricultural income 620000.00
    """.trimIndent()

    val foreignTaxProof = """
        Withholding tax statement, United States
        Foreign income 1850000.00
        Tax withheld 275000.00
    """.trimIndent()

    val winningsCertificate = """
        Certificate for winnings from an online game
        Gross winnings 250000.00
        Tax deducted 75000.00
    """.trimIndent()

    val panCard = """
        INCOME TAX DEPARTMENT GOVT. OF INDIA
        Permanent Account Number Card
        ABCDE1234F
        Ravi Kumar
        Date of Birth 01/01/1990
    """.trimIndent()

    val aadhaar = """
        Government of India
        Unique Identification Authority of India
        Aadhaar
        2345 6789 0123
        Ravi Kumar
    """.trimIndent()

    val bankAccountProof = """
        State Bank of India
        Cancelled cheque
        Account Number 12345678901
        IFSC SBIN0001234
        Payable at par
    """.trimIndent()

    val taxPaymentChallan = """
        Challan Identification Number, ITNS 280
        Type of payment (300) Self Assessment Tax
        BSR Code 0510308
        Challan Serial Number 54321
        Date 28/06/2026
        Total amount 45000.00
    """.trimIndent()

    val taxAuditReport = """
        FORM NO. 3CD
        Statement of particulars required to be furnished under section 44AB
        UDIN 21123456AAAABB1234
        Total turnover 8450000.00
        Net profit 1370000.00
    """.trimIndent()

    val form10iea = """
        FORM NO. 10-IEA
        Application for exercise of option under section 115BAC(6)
        Acknowledgement Number 123456789012345
        Date of filing 15/06/2026
    """.trimIndent()

    val salarySlip = """
        Salary slip for March 2026
        Acme Software Private Limited
        Gross earnings 158000.00
        Professional tax 200.00
        Income tax 18267.00
    """.trimIndent()

    val form12bb = """
        FORM NO. 12BB
        Statement showing particulars of claims by an employee for deduction of tax
        Rent paid to the landlord 360000.00
        Permanent Account Number of the landlord ZZZZZ9999Z
        Interest payable to the lender 198000.00
        Section 80C 150000.00
    """.trimIndent()

    val form27d = """
        FORM NO. 27D
        Certificate under section 206C of the Income-tax Act, 1961
        Name and address of the collector
        HDFC Bank Limited
        TAN of the Collector: MUMH12345C
        Amount of tax collected 25000.00
    """.trimIndent()

    val form15h = """
        FORM NO. 15H
        Declaration under section 197A(1C) to be made by an individual who is of the age of sixty years or more
        State Bank of India
    """.trimIndent()

    val tradingProfitAndLoss = """
        Tax P&L statement FY 2025-26
        Zerodha Broking Limited
        Intraday profit 84000.00
        Futures and options profit 265000.00
        Short term capital gain 48000.00
        Long term capital gain 172000.00
    """.trimIndent()
}