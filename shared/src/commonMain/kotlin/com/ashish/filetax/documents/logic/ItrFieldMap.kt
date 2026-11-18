package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.documents.model.ItrField

object ItrFieldMap {

    fun forKind(kind: FigureKind): ItrField = when (kind) {
        FigureKind.GROSS_SALARY -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Gross salary, row 1(a): salary as per section 17(1)",
            note = "In ITR-1 it is the Gross Salary box under Income from Salary. Prefilled data is often stale, so type the Form 16 figure yourself."
        )

        FigureKind.PERQUISITES -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Gross salary, row 1(b): value of perquisites under section 17(2)",
            note = "Rent-free accommodation, a company car, ESOPs and similar benefits, itemised in Form 12BA. It is already inside the gross salary, so do not add it twice."
        )

        FigureKind.ESOP_PERQUISITE -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Perquisites under section 17(2): shares allotted under an ESOP",
            note = "The difference between the market price on the day you exercised and what you paid. If the company is an eligible start-up, the tax can be deferred and the details go in Schedule TTI."
        )

        FigureKind.EXEMPT_ALLOWANCES -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Allowances exempt under section 10",
            note = "HRA, leave travel and the rest. Pick the right sub-head for each allowance. Under the new regime almost all of these go away."
        )

        FigureKind.LEAVE_ENCASHMENT -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Allowances exempt under section 10: leave encashment under 10(10AA)",
            note = "Encashment while in service is fully taxable. On leaving, a non-government employee is exempt up to ₹25,00,000 over a lifetime, or the smaller of ten months' average salary and the actual amount. Enter the whole amount in salary and the exempt part here."
        )

        FigureKind.GRATUITY -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Allowances exempt under section 10: gratuity under 10(10)",
            note = "Exempt up to ₹20,00,000 over a lifetime for an employee covered by the Gratuity Act, and fully exempt for a government employee. Anything above the limit is salary."
        )

        FigureKind.PENSION_COMMUTATION -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Allowances exempt under section 10: commuted pension under 10(10A)",
            note = "A third of the pension is exempt if you also get gratuity, half if you do not. A government pension commuted is fully exempt."
        )

        FigureKind.VRS_COMPENSATION -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Allowances exempt under section 10: voluntary retirement under 10(10C)",
            note = "Exempt up to ₹5,00,000 once in a lifetime, and only if the scheme met the rule 2BA conditions. If you claim this, relief under section 89 cannot be claimed on the same amount."
        )

        FigureKind.NOTICE_PAY_RECOVERY -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Salary, reduced by the recovery only if Form 16 already shows it net",
            note = "Notice pay cut by your employer is not a deduction of its own. If Form 16 shows the gross salary before the recovery, the department expects the gross figure."
        )

        FigureKind.SALARY_ARREARS -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Gross salary, row 1(a), with the break-up in Form 10E",
            note = "Arrears are taxed in the year you received them. File Form 10E on the portal before the return, or relief under section 89 will be refused."
        )

        FigureKind.RELIEF_SECTION_89 -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Part B-TTI: relief under section 89",
            note = "Claimable only if Form 10E has been filed separately on the portal for this year. The relief cuts the tax, not the income."
        )

        FigureKind.STANDARD_DEDUCTION -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Deductions under section 16(ia): standard deduction",
            note = "Flat deduction against salary or pension. The portal fills it, so check it matches Form 16 rather than typing over it."
        )

        FigureKind.PROFESSIONAL_TAX -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Deductions under section 16(iii): tax on employment",
            note = "Professional tax your employer paid to the state. Allowed only under the old regime."
        )

        FigureKind.INCOME_CHARGEABLE_UNDER_SALARY -> ItrField(
            schedule = ItrSchedule.COMPUTED,
            field = "Part B-TI, row 1: income from salary",
            note = "Gross salary less exemptions and section 16 deductions. Use it to confirm the portal's number matches Form 16."
        )

        FigureKind.HOUSE_PROPERTY_INCOME -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Income or loss from house property",
            note = "A negative figure is a loss, usually home loan interest on a self-occupied house. Set the property as self-occupied or let out before entering it."
        )

        FigureKind.OTHER_INCOME_REPORTED_TO_EMPLOYER -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Income from other sources",
            note = "What you had declared to your employer. Your own AIS and bank figures are the fuller record, so reconcile before entering."
        )

        FigureKind.GROSS_TOTAL_INCOME -> ItrField(
            schedule = ItrSchedule.COMPUTED,
            field = "Part B-TI: gross total income",
            note = "A totals row. Worth comparing with Form 16 once every head of income is filled in."
        )

        FigureKind.DEDUCTION_80C -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80C",
            note = "EPF, PPF, life insurance, ELSS, home loan principal, tuition fees, capped at ₹1,50,000. Old regime only."
        )

        FigureKind.DEDUCTION_80CCD1B -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80CCD(1B)",
            note = "Your own NPS contribution, up to ₹50,000 over and above 80C. Old regime only."
        )

        FigureKind.DEDUCTION_80CCD2 -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80CCD(2)",
            note = "Your employer's NPS contribution, up to 14 percent of basic and dearness allowance. This one survives under the new regime, so do not drop it."
        )

        FigureKind.DEDUCTION_80D -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80D",
            note = "Health insurance premium for you and your parents. Old regime only."
        )

        FigureKind.DEDUCTION_80DDB -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80DDB",
            note = "Treatment of a specified illness, up to ₹40,000, or ₹1,00,000 for a senior citizen. A prescription from the treating specialist is mandatory. Old regime only."
        )

        FigureKind.DEDUCTION_80E -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80E",
            note = "Interest on an education loan, with no ceiling, for eight years from the first repayment. Old regime only."
        )

        FigureKind.DEDUCTION_80G -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80G, with the donee's PAN and address in the 80G table",
            note = "Half or all of the donation qualifies, depending on the institution. The table now also insists on the transaction reference number and the bank IFSC. Cash donations above ₹2,000 do not count. Old regime only."
        )

        FigureKind.DEDUCTION_80GG -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80GG",
            note = "Rent paid when you get no HRA, capped at ₹60,000 a year. Form 10BA has to be filed on the portal first. Old regime only."
        )

        FigureKind.DEDUCTION_80TTA_TTB -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80TTA, or 80TTB if you are a senior citizen",
            note = "Savings bank interest up to ₹10,000, or ₹50,000 of interest for senior citizens. Enter the interest in Schedule OS first. Old regime only."
        )

        FigureKind.TOTAL_CHAPTER_VIA_DEDUCTIONS -> ItrField(
            schedule = ItrSchedule.COMPUTED,
            field = "Part B-TI: total Chapter VI-A deductions",
            note = "The sum of everything in Schedule VI-A. Compare it with Form 16 after entering each deduction."
        )

        FigureKind.TOTAL_TAXABLE_INCOME -> ItrField(
            schedule = ItrSchedule.COMPUTED,
            field = "Part B-TI: total income",
            note = "If this does not match Form 16, an income or a deduction is still missing."
        )

        FigureKind.TDS_ON_SALARY -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule TDS1: tax deducted at source from salary",
            note = "Enter it against your employer's TAN. It must match Form 26AS, because only what reached the department can be claimed."
        )

        FigureKind.TDS_OTHER_THAN_SALARY -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule TDS2: tax deducted at source other than salary",
            note = "Bank interest, rent, professional fees, sale of property and the like, entered against each deductor's TAN from Form 26AS. Since 1 April 2025 a firm also deducts 10 percent under section 194T on what it pays a partner, so a partner should find that entry here too."
        )

        FigureKind.TCS -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule TCS: tax collected at source",
            note = "Collected on foreign remittances, car purchases and similar. Claim it against the collector's TAN."
        )

        FigureKind.ADVANCE_TAX -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule IT: advance tax paid",
            note = "Enter the BSR code, challan number and date of each challan exactly as Form 26AS shows them."
        )

        FigureKind.SELF_ASSESSMENT_TAX -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule IT: self assessment tax paid",
            note = "Same challan details as advance tax. Any balance tax you pay before filing goes here too."
        )

        FigureKind.SAVINGS_INTEREST -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Interest from savings bank accounts",
            note = "Every savings account, not only the ones with TDS. Then claim it under 80TTA or 80TTB if you are on the old regime."
        )

        FigureKind.DEPOSIT_INTEREST -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Interest from deposits: bank, post office and cooperative society",
            note = "Interest accrued for the year, even if the deposit has not matured, if that is how you have always reported it."
        )

        FigureKind.OTHER_INTEREST -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Any other interest income",
            note = "Interest on an income tax refund, bonds, or loans you gave. It is taxable even though no TDS was cut."
        )

        FigureKind.DIVIDEND -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Dividend income",
            note = "Taxable in your hands at slab rate. Also fill the quarterly break-up lower in Schedule OS, or interest under section 234C gets added."
        )

        FigureKind.RENT_RECEIVED -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Gross rent received or receivable",
            note = "Enter it per property, then municipal taxes paid and the 30 percent standard deduction, and the tenant's PAN if rent crosses ₹50,000 a month."
        )

        FigureKind.MUNICIPAL_TAX_PAID -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Municipal taxes paid during the year",
            note = "Deductible from the gross rent only if you actually paid it in the year, not merely that it fell due. Nothing is allowed for a self-occupied house."
        )

        FigureKind.HOME_LOAN_INTEREST_PAID -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Interest payable on borrowed capital",
            note = "Capped at ₹2,00,000 for a self-occupied house and allowed only under the old regime. A let-out property has no cap, but the set-off against other income is limited to ₹2,00,000."
        )

        FigureKind.HOME_LOAN_PRINCIPAL_PAID -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80C: repayment of housing loan principal",
            note = "The principal part of your EMIs shares the ₹1,50,000 section 80C ceiling with everything else."
        )

        FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Section 111A: short term gain on listed equity and equity mutual funds",
            note = "A flat 20 percent for the whole of FY 2025-26. The split between sales before and after 23 July 2024 is gone from the schedule this year, and the section 87A rebate never applies to this gain."
        )

        FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Section 112A: long term gain on listed equity and equity mutual funds",
            note = "First ₹1,25,000 of such gains is exempt, the rest is taxed at 12.5 percent, and the section 87A rebate does not touch it. Schedule 112A wants the sale scrip by scrip. Up to ₹1,25,000 with no loss to carry forward, ITR-1 can still hold it."
        )

        FigureKind.SALE_OF_SECURITIES -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Full value of consideration on sale of shares and mutual fund units",
            note = "This is sale value, not gain. Use your broker's capital gains statement for cost and gain, and reconcile it against this AIS figure."
        )

        FigureKind.SALE_OF_IMMOVABLE_PROPERTY -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Sale of land or building: full value of consideration",
            note = "Enter the buyer's name, PAN, address and share as well. Any exemption under section 54, 54F or 54EC goes in the same schedule, along with the deposit account if you have not reinvested yet."
        )

        FigureKind.PURCHASE_OF_IMMOVABLE_PROPERTY -> ItrField(
            schedule = ItrSchedule.NO_ENTRY,
            field = "Reported in AIS as a high value transaction",
            note = "A purchase is not income, so nothing goes in the return. Keep the papers, and if your total income crosses ₹50 lakh the asset goes in Schedule AL."
        )

        FigureKind.STAMP_DUTY_VALUE -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Sale of land or building: stamp duty value",
            note = "If the stamp duty value is more than 110 percent of the sale price, section 50C makes the stamp duty value the sale value for tax."
        )

        FigureKind.COST_OF_ACQUISITION -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Cost of acquisition, with improvement cost and transfer expenses",
            note = "Long term gains are 12.5 percent without indexation now. The one exception: land or a building bought before 23 July 2024 by a resident individual or HUF can still be taxed at 20 percent with indexation if that comes out lower. For property bought before 1 April 2001, the fair value on that date may be used as cost instead."
        )

        FigureKind.VDA_SALE_CONSIDERATION -> ItrField(
            schedule = ItrSchedule.VIRTUAL_DIGITAL_ASSETS,
            field = "Schedule VDA: consideration received, transfer by transfer",
            note = "Every sale of crypto or an NFT is listed on its own, with the date of acquisition and the date of transfer."
        )

        FigureKind.VDA_COST_OF_ACQUISITION -> ItrField(
            schedule = ItrSchedule.VIRTUAL_DIGITAL_ASSETS,
            field = "Schedule VDA: cost of acquisition",
            note = "Only what you paid for the asset. Exchange fees, gas fees and interest are not allowed, and a loss on one coin cannot be set off against a gain on another."
        )

        FigureKind.VDA_INCOME -> ItrField(
            schedule = ItrSchedule.VIRTUAL_DIGITAL_ASSETS,
            field = "Schedule VDA: income from transfer of virtual digital assets",
            note = "Flat 30 percent under section 115BBH, with no deduction other than cost and no set-off of any loss. The 1 percent TDS under 194S is claimed in Schedule TDS2."
        )

        FigureKind.BUSINESS_RECEIPTS -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Gross receipts or turnover",
            note = "Under section 44AD or 44ADA in ITR-4 you declare presumptive income on this turnover. With books, it goes through Schedule BP instead."
        )

        FigureKind.DIGITAL_TURNOVER -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Section 44AD: turnover received through banking channels",
            note = "Presumptive income on this part is 6 percent, against 8 percent on cash. Declaring less than that means books and an audit."
        )

        FigureKind.CASH_TURNOVER -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Section 44AD: turnover received in cash",
            note = "Presumptive income on this part is 8 percent. Cash receipts also decide whether the ₹2 crore or the ₹3 crore presumptive ceiling applies to you."
        )

        FigureKind.GST_TURNOVER -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "GSTIN and turnover as per the GST return",
            note = "The return asks for the turnover you reported under GST against each GSTIN. A gap between this and your books is the most common notice."
        )

        FigureKind.BUSINESS_GROSS_PROFIT -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Profit and loss account: gross profit",
            note = "Part A-P&L is filled line by line when you keep books. In the no-accounts case, only turnover, gross profit, expenses and net profit are asked for."
        )

        FigureKind.BUSINESS_EXPENSES -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Profit and loss account: total expenses",
            note = "Break it into the heads the schedule asks for. Payments where TDS was due but not cut get disallowed under section 40(a)(ia)."
        )

        FigureKind.DEPRECIATION -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule DPM and DOA, and the depreciation row in the P&L",
            note = "Book depreciation is added back in Schedule BP and income tax depreciation is allowed instead, block by block."
        )

        FigureKind.BUSINESS_NET_PROFIT -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Profit and loss account: net profit, carried into Schedule BP",
            note = "Schedule BP starts from this profit, then adds back what is disallowed and subtracts income taxed under another head."
        )

        FigureKind.CLOSING_STOCK -> ItrField(
            schedule = ItrSchedule.BALANCE_SHEET,
            field = "Part A-BS: inventories",
            note = "The balance sheet as on 31 March. In the no-accounts case only debtors, creditors, stock and cash are asked for."
        )

        FigureKind.SUNDRY_DEBTORS -> ItrField(
            schedule = ItrSchedule.BALANCE_SHEET,
            field = "Part A-BS: sundry debtors",
            note = "What your customers still owe you on 31 March."
        )

        FigureKind.SUNDRY_CREDITORS -> ItrField(
            schedule = ItrSchedule.BALANCE_SHEET,
            field = "Part A-BS: sundry creditors",
            note = "What you still owe your suppliers on 31 March."
        )

        FigureKind.CASH_BALANCE -> ItrField(
            schedule = ItrSchedule.BALANCE_SHEET,
            field = "Part A-BS: cash and bank balance",
            note = "Cash in hand plus the balance in every business bank account on 31 March."
        )

        FigureKind.PARTNER_SHARE_OF_PROFIT -> ItrField(
            schedule = ItrSchedule.EXEMPT,
            field = "Schedule EI: share of profit from a firm, exempt under section 10(2A)",
            note = "The firm has already paid tax on it, so it is exempt in your hands, but it still has to be reported. The firm's PAN and your share go in Schedule IF of ITR-3."
        )

        FigureKind.PARTNER_REMUNERATION -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule BP: remuneration from the firm",
            note = "Salary or commission from a firm you are a partner in is business income for you, not salary, and ITR-3 is the form for it."
        )

        FigureKind.PARTNER_INTEREST_ON_CAPITAL -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule BP: interest on capital from the firm",
            note = "Taxable as business income, to the extent the firm was allowed to deduct it, which is 12 percent a year at most."
        )

        FigureKind.WINNINGS -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Income chargeable at a special rate: winnings",
            note = "Flat 30 percent with no deduction and no basic exemption. Enter the gross amount, not what reached your account."
        )

        FigureKind.AGRICULTURAL_INCOME -> ItrField(
            schedule = ItrSchedule.EXEMPT,
            field = "Schedule EI: agricultural income",
            note = "Exempt, but above ₹5,00,000 it is used to work out the rate on your other income, and the schedule then asks for the land details and whether it is irrigated."
        )

        FigureKind.FOREIGN_INCOME -> ItrField(
            schedule = ItrSchedule.FOREIGN,
            field = "Schedule FSI: income from outside India, country by country",
            note = "A resident is taxed on worldwide income, so it goes into the right head here as well. Only ITR-2 and ITR-3 carry this schedule."
        )

        FigureKind.FOREIGN_TAX_PAID -> ItrField(
            schedule = ItrSchedule.FOREIGN,
            field = "Schedule TR: tax relief for tax paid outside India",
            note = "Form 67 has to be filed on the portal before you file the return, or the credit is refused. Keep the foreign tax certificate."
        )

        FigureKind.FOREIGN_ASSET_VALUE -> ItrField(
            schedule = ItrSchedule.FOREIGN,
            field = "Schedule FA: foreign assets and accounts",
            note = "Every foreign account, share, ESOP and property held at any time in the calendar year, with peak balance and closing value. Leaving it out carries a ₹10 lakh penalty even when no tax is due."
        )

        FigureKind.UNLISTED_SHARE_COST -> ItrField(
            schedule = ItrSchedule.NO_ENTRY,
            field = "Schedule SH-1 or the unlisted shares table in Part A",
            note = "Holding unlisted shares is not income, but it has to be declared: the company's name, PAN, shares held, cost, and anything bought or sold during the year. It also rules ITR-1 and ITR-4 out."
        )

        FigureKind.LAST_YEAR_TOTAL_INCOME -> ItrField(
            schedule = ItrSchedule.NO_ENTRY,
            field = "Last year's acknowledgement, kept for reference",
            note = "Useful to check that nothing has been dropped this year, and to carry the right opening figures into the balance sheet and the loss schedules."
        )

        FigureKind.CARRIED_FORWARD_LOSS -> ItrField(
            schedule = ItrSchedule.LOSSES,
            field = "Schedule CFL: loss carried forward from an earlier year",
            note = "Enter it year by year, exactly as last year's return showed it. A loss can only be carried forward if that return was filed by the due date."
        )

        FigureKind.FAMILY_PENSION -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Family pension, with the deduction under section 57(iia) beside it",
            note = "A third of the pension comes off, capped at ₹25,000 under the new regime and ₹15,000 under the old. It is other-sources income, not salary, even though it reaches you the same way."
        )

        FigureKind.FOREIGN_RETIREMENT_ACCOUNT_INCOME -> ItrField(
            schedule = ItrSchedule.FOREIGN,
            field = "Schedule OS with the section 89A relief flag, and Schedule FA for the account itself",
            note = "Income accruing in a 401(k) or similar notified foreign retirement account can be deferred until you withdraw it. From AY 2026-27 this reporting was taken out of ITR-1 and ITR-4, so it needs ITR-2 or ITR-3."
        )

        FigureKind.UNREALISED_RENT -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Rent which cannot be realised, the new row under gross rent",
            note = "Rent your tenant never paid comes off the gross rent, but only if you actually tried to recover it. If it is recovered later, it is taxed in that year."
        )

        FigureKind.BUYBACK_PROCEEDS -> ItrField(
            schedule = ItrSchedule.OTHER_SOURCES,
            field = "Dividend income: buyback proceeds treated as a deemed dividend under section 2(22)(f)",
            note = "The company no longer pays the tax on a buyback. The whole amount you received is taxed as a dividend at your slab rate, and the cost of those shares becomes a capital loss in Schedule CG."
        )

        FigureKind.DEDUCTION_80GGC -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80GGC, with the party's name and PAN",
            note = "A contribution to a political party or an electoral trust, allowed in full but never in cash. The return now asks who received it, and these claims are checked closely. Old regime only."
        )

        FigureKind.DEDUCTION_80DD_80U -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80DD for a dependant with a disability, or 80U for your own",
            note = "A flat ₹75,000, or ₹1,25,000 for a severe disability, whatever the actual spend. Form 10-IA from the medical authority has to be filed on the portal. Old regime only."
        )

        FigureKind.DEDUCTION_80EE_80EEA -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80EE or 80EEA: extra interest on a home loan",
            note = "Up to ₹50,000 under 80EE, or ₹1,50,000 under 80EEA, on top of the ₹2,00,000 in Schedule HP, and only for loans sanctioned in the qualifying windows. Old regime only."
        )

        FigureKind.DEDUCTION_80EEB -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80EEB: interest on an electric vehicle loan",
            note = "Up to ₹1,50,000 a year, for a loan sanctioned between 1 April 2019 and 31 March 2023. Old regime only."
        )

        FigureKind.SPECULATIVE_BUSINESS_INCOME -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule BP: income from speculative business",
            note = "Intraday equity trading is speculative business, kept apart from everything else. Its loss can only be set off against speculative profit, and only for four years."
        )

        FigureKind.DERIVATIVES_INCOME -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule BP: income from futures and options",
            note = "Non-speculative business income, with its own reporting fields this year. Its loss sets off against any income except salary, and carries forward for eight years if you file by the due date."
        )

        FigureKind.FOREIGN_REMITTANCE -> ItrField(
            schedule = ItrSchedule.NO_ENTRY,
            field = "Reported in AIS as an outward remittance",
            note = "Sending money abroad is not income. Any tax collected on it is claimed in Schedule TCS."
        )
    }
}