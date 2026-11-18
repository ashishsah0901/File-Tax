package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.itr.ItrSchedule
import com.ashish.filetax.onboarding.model.FilingStep
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TaxYear
import com.ashish.filetax.tutorial.TutorialAnimation

object FilingGuide {

    fun forForm(form: ItrForm, answers: OnboardingAnswers): List<FilingStep> {
        val steps = when (form) {
            ItrForm.ITR1 -> itr1Steps(answers)
            ItrForm.ITR2 -> itr2Steps(answers)
            ItrForm.ITR3 -> itr3Steps(answers)
            ItrForm.ITR4 -> itr4Steps(answers)
        }
        val checklistIds = DocumentChecklist.forAnswers(answers).map { it.id }.toSet()
        return steps.map { step ->
            step.copy(documentIds = step.documentIds.filter { it in checklistIds })
        }
    }

    private fun itr1Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(mustYouFile("itr1"))
        add(login("itr1"))
        add(startReturn("itr1", ItrForm.ITR1))
        add(regime("itr1", hasBusinessIncome = false))
        add(scheduleSelection("itr1", ItrForm.ITR1, answers))
        salary("itr1", answers)?.let(::add)
        houseProperty("itr1", answers)?.let(::add)
        add(otherIncome("itr1", answers))
        add(deductions("itr1"))
        add(taxesPaid("itr1", hasBusinessIncome = false))
        add(payTax("itr1"))
        add(previewSubmit("itr1"))
        add(everify("itr1"))
        add(deadlines("itr1", ItrForm.ITR1))
    }

    private fun itr2Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(mustYouFile("itr2"))
        add(login("itr2"))
        add(startReturn("itr2", ItrForm.ITR2))
        add(regime("itr2", hasBusinessIncome = false))
        add(scheduleSelection("itr2", ItrForm.ITR2, answers))
        salary("itr2", answers)?.let(::add)
        houseProperty("itr2", answers)?.let(::add)
        capitalGains("itr2", answers)?.let(::add)
        cryptoAssets("itr2", answers)?.let(::add)
        add(otherIncome("itr2", answers))
        exemptIncome("itr2", answers)?.let(::add)
        broughtForwardLosses("itr2", answers)?.let(::add)
        foreignAssets("itr2", answers)?.let(::add)
        foreignTaxRelief("itr2", answers)?.let(::add)
        specialSchedules("itr2", answers)?.let(::add)
        add(deductions("itr2"))
        add(taxesPaid("itr2", hasBusinessIncome = false))
        add(payTax("itr2"))
        add(previewSubmit("itr2"))
        add(everify("itr2"))
        add(deadlines("itr2", ItrForm.ITR2))
    }

    private fun itr3Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(mustYouFile("itr3"))
        add(login("itr3"))
        add(startReturn("itr3", ItrForm.ITR3))
        add(regime("itr3", hasBusinessIncome = true))
        add(scheduleSelection("itr3", ItrForm.ITR3, answers))
        add(
            FilingStep(
                id = "itr3_business_books",
                schedules = listOf(ItrSchedule.BUSINESS, ItrSchedule.BALANCE_SHEET),
                animation = TutorialAnimation.LEDGER,
                tip = "Part A-P&L and Part A-BS come before Schedule BP, and the portal will not let you past them while a total does not tie.",
                title = "Enter the business income from your books",
                detail = "Part A-P&L and Part A-BS want the year's figures line by line, and Schedule BP then starts from the net profit, adds back what is disallowed and takes out income taxed under another head. Book depreciation is added back and income tax depreciation allowed instead, block by block in Schedules DPM and DOA. Keep the turnover consistent with your GST returns — the department matches the two — and fill the investments and bank balance fields that the financial particulars block now insists on. An audit under section 44AB is due once turnover crosses ₹1 crore, or ₹10 crore when cash receipts and payments both stay within 5%, and for a profession once receipts cross ₹50 lakh; the auditor files Form 3CA or 3CB with 3CD by 30 September 2026 and you accept it on the portal before filing.",
                documentIds = listOf(
                    DocumentCatalog.booksOfAccounts.id,
                    DocumentCatalog.businessBankStatements.id,
                    DocumentCatalog.gstReturns.id
                )
            )
        )
        if (answers.businessKinds.orEmpty().any { it.isPresumptive }) {
            add(
                FilingStep(
                    id = "itr3_presumptive",
                    schedules = listOf(ItrSchedule.BUSINESS),
                    animation = TutorialAnimation.PRESUMPTIVE,
                    tip = "Split the turnover into cash and banking receipts before you start, because the two are presumed at different rates.",
                    title = "Declare the presumptive part",
                    detail = "Any part of the business you declare at the fixed presumptive rate goes into its own section of Schedule BP. Enter the turnover split into digital and cash receipts — the digital share is taxed at the lower rate.",
                    documentIds = listOf(DocumentCatalog.turnoverSummary.id)
                )
            )
        }
        if (answers.has(BusinessKind.SPECULATIVE_OR_DERIVATIVES)) {
            add(
                FilingStep(
                    id = "itr3_trading",
                    schedules = listOf(ItrSchedule.BUSINESS),
                    animation = TutorialAnimation.TRADING,
                    tip = "Turnover for futures and options is the sum of profits and losses, not the contract value. Your broker's tax P&L already works it out.",
                    title = "Report your trading as business income",
                    detail = "Intraday trades are speculative business and futures and options are normal business — the two go into separate parts of Schedule BP, and this year futures and options have their own reporting fields. A speculative loss can only be set off against speculative profit, and only for four years. Work the turnover and profit off your broker's tax P&L, not memory.",
                    documentIds = listOf(DocumentCatalog.brokerStatement.id)
                )
            )
        }
        if (answers.has(BusinessKind.PARTNER_IN_FIRM)) {
            add(
                FilingStep(
                    id = "itr3_partner",
                    schedules = listOf(ItrSchedule.BUSINESS, ItrSchedule.EXEMPT),
                    animation = TutorialAnimation.PARTNER,
                    tip = "Schedule IF wants the firm's PAN. Your remuneration goes under Schedule BP, never under salary.",
                    title = "Enter your share from the firm",
                    detail = "The firm's name, PAN and your profit share go into Schedule IF. Your share of profit is exempt under section 10(2A) and goes in Schedule EI, but the remuneration and the interest on capital the firm paid you are business income in Schedule BP — split them exactly as the firm's statement does. Since 1 April 2025 the firm also has to deduct 10% under section 194T once those payments cross ₹20,000 in the year, so check that credit in Form 26AS.",
                    documentIds = listOf(DocumentCatalog.firmShareStatement.id)
                )
            )
        }
        salary("itr3", answers)?.let(::add)
        houseProperty("itr3", answers)?.let(::add)
        capitalGains("itr3", answers)?.let(::add)
        cryptoAssets("itr3", answers)?.let(::add)
        add(otherIncome("itr3", answers))
        exemptIncome("itr3", answers)?.let(::add)
        broughtForwardLosses("itr3", answers)?.let(::add)
        foreignAssets("itr3", answers)?.let(::add)
        foreignTaxRelief("itr3", answers)?.let(::add)
        specialSchedules("itr3", answers)?.let(::add)
        add(deductions("itr3"))
        add(taxesPaid("itr3", hasBusinessIncome = true))
        add(payTax("itr3"))
        add(previewSubmit("itr3"))
        add(everify("itr3"))
        add(deadlines("itr3", ItrForm.ITR3))
    }

    private fun itr4Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(mustYouFile("itr4"))
        add(login("itr4"))
        add(startReturn("itr4", ItrForm.ITR4))
        add(regime("itr4", hasBusinessIncome = true))
        add(scheduleSelection("itr4", ItrForm.ITR4, answers))
        add(
            FilingStep(
                id = "itr4_presumptive",
                schedules = listOf(ItrSchedule.BUSINESS),
                animation = TutorialAnimation.PRESUMPTIVE,
                tip = "Split the turnover into cash and banking receipts before you start, because the two are presumed at different rates.",
                title = "Declare income at the presumptive rate",
                detail = "Enter the year's turnover split into digital and cash receipts. Business income is presumed at 6% of the digital share and 8% of the cash share; professional fees at 50%; a goods vehicle at ₹1,000 per tonne per month for a heavy vehicle and ₹7,500 a month otherwise. Section 44AD stops at ₹2 crore of turnover, or ₹3 crore when cash receipts and cash payments both stay within 5%; section 44ADA at ₹50 lakh, or ₹75 lakh on the same condition; section 44AE at ten vehicles. You can declare more than the presumed rate, never less: declare less with income above the basic exemption and you need books and an audit, and section 44AD then keeps you out of the scheme for the next five years. The turnover should match your GST returns if you are registered, and Sugam still wants your debtors, creditors, stock and cash balance on 31 March.",
                documentIds = listOf(
                    DocumentCatalog.turnoverSummary.id,
                    DocumentCatalog.businessBankStatements.id,
                    DocumentCatalog.gstReturns.id
                )
            )
        )
        salary("itr4", answers)?.let(::add)
        houseProperty("itr4", answers)?.let(::add)
        add(otherIncome("itr4", answers))
        add(deductions("itr4"))
        add(taxesPaid("itr4", hasBusinessIncome = true))
        add(payTax("itr4"))
        add(previewSubmit("itr4"))
        add(everify("itr4"))
        add(deadlines("itr4", ItrForm.ITR4))
    }

    private fun mustYouFile(prefix: String) = FilingStep(
        id = "${prefix}_must_you_file",
        animation = TutorialAnimation.ELIGIBILITY,
        tip = "If you are not sure, open AIS first. It is the department's own list of what it already knows about your year.",
        title = "Make sure you have to file at all",
        detail = "You have to file once your income before deductions crosses the basic exemption: ₹4,00,000 under the new regime, or ₹2,50,000 under the old, ₹3,00,000 at sixty and ₹5,00,000 at eighty. Below that you still have to file if any of these happened: you hold an asset or an account outside India, your business turnover crossed ₹60 lakh, professional receipts crossed ₹10 lakh, TDS and TCS on you added up to ₹25,000 or more, ₹50,000 for a senior citizen, deposits in your savings accounts crossed ₹50 lakh, deposits in a current account crossed ₹1 crore, you spent over ₹2 lakh on foreign travel, or over ₹1 lakh on electricity. And file anyway if tax was cut from your income, because a refund only comes through a return.",
        documentIds = listOf(DocumentCatalog.form26as.id, DocumentCatalog.annualInformationStatement.id)
    )

    private fun login(prefix: String) = FilingStep(
        id = "${prefix}_login",
        schedules = listOf(ItrSchedule.FILING_DETAILS),
        animation = TutorialAnimation.LOGIN,
        tip = "The dashboard is not the return. The form lives under e-File, then Income Tax Returns, then File Income Tax Return.",
        title = "Log in to the portal",
        detail = "Go to incometax.gov.in and log in with your PAN as the user ID. While you are there, open Profile and check that your bank account shows as validated — a refund cannot arrive without it.",
        documentIds = listOf(DocumentCatalog.pan.id, DocumentCatalog.bankAccount.id)
    )

    private fun startReturn(prefix: String, form: ItrForm) = FilingStep(
        id = "${prefix}_start_return",
        animation = TutorialAnimation.PICK_FORM,
        tip = "Pick Online mode. The offline utility is only worth it on a slow connection, or with a very long list of capital gains.",
        title = "Start the return",
        detail = "Go to e-File, then Income Tax Returns, then File Income Tax Return. Pick AY ${TaxYear.ASSESSMENT_YEAR}, Online mode, Individual, and ${form.label} when it asks which form."
    )

    private fun regime(prefix: String, hasBusinessIncome: Boolean) = FilingStep(
        id = "${prefix}_regime",
        animation = TutorialAnimation.REGIME,
        tip = "The regime choice sits at the top of the personal information screen, and the portal shows you both computations before you commit.",
        title = "Confirm your details and pick a regime",
        detail = buildString {
            append("Check the pre-filled name, address, email and mobile — a second phone number, email and address are compulsory this year, and the Aadhaar enrolment ID is no longer accepted in place of the number. ")
            append("Then choose between the new and old regime. The new one is the default: nothing up to ₹4,00,000, then 5% to ₹8,00,000, 10% to ₹12,00,000, 15% to ₹16,00,000, 20% to ₹20,00,000, 25% to ₹24,00,000 and 30% above that, with a ₹75,000 standard deduction against salary and a rebate under section 87A of up to ₹60,000 that takes tax to nil up to ₹12,00,000 of income, ₹12,75,000 for a salaried person. ")
            append("The old regime starts taxing at ₹2,50,000, ₹3,00,000 once you are sixty and ₹5,00,000 once you are eighty, with a ₹50,000 standard deduction and a rebate of ₹12,500 up to ₹5,00,000 of income, so it only wins if your 80C, 80D, HRA and loan interest together beat the new regime's lower rates. Under either regime the rebate never applies to capital gains taxed at the special rates.")
            if (hasBusinessIncome) {
                append(" With business income there is one extra catch: opting out of the new regime needs Form 10-IEA filed on the portal before you file the return, its acknowledgement number goes into Part A, and once you go back to the new regime you cannot opt out again while the business runs.")
            }
        },
        documentIds = listOf(DocumentCatalog.deductionProofs.id)
    )

    private fun scheduleSelection(prefix: String, form: ItrForm, answers: OnboardingAnswers) = FilingStep(
        id = "${prefix}_schedules",
        animation = TutorialAnimation.EXTRA_SCHEDULES,
        tip = "A schedule you did not tick simply never appears, and the return will not tell you it is missing.",
        title = if (form == ItrForm.ITR1 || form == ItrForm.ITR4) {
            "Confirm the parts that apply to you"
        } else {
            "Tick the schedules you need"
        },
        detail = if (form == ItrForm.ITR1 || form == ItrForm.ITR4) {
            "${form.label} has no schedule selection screen. It walks you through the sections below in order, and each one opens only if it applies to you. Check the list against your answers before you start, so you notice anything the portal does not ask for."
        } else {
            "After the personal information screen the portal asks which schedules you want. Only what you tick here opens later, and nothing appears on its own afterwards. Work down this list and tick every one of them."
        },
        schedulePicks = ScheduleChecklist.forForm(form, answers)
    )

    private fun salary(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.SALARY_PENSION)) return null
        return FilingStep(
            id = "${prefix}_salary",
            schedules = listOf(ItrSchedule.SALARY),
            animation = TutorialAnimation.SALARY,
            tip = "Expand the salary schedule rather than trusting the summary. The break-up underneath is what has to match Form 16.",
            title = "Check the pre-filled salary",
            detail = "The salary schedule comes pre-filled from what your employer reported. Match it line by line against Part B of your Form 16 — gross salary, exempt allowances and the standard deduction — and fix anything that differs. Changed jobs? Add every employer.",
            documentIds = listOf(DocumentCatalog.form16.id)
        )
    }

    private fun houseProperty(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.HOUSE_PROPERTY)) return null
        val multiple = answers.housePropertyCount != HousePropertyCount.ONE
        return FilingStep(
            id = "${prefix}_house_property",
            schedules = listOf(ItrSchedule.HOUSE_PROPERTY),
            animation = TutorialAnimation.HOUSE,
            tip = "Each property is added separately, and the self-occupied or let-out choice changes which boxes open underneath.",
            title = if (multiple) "Fill in each house property" else "Fill in your house property",
            detail = buildString {
                if (multiple) {
                    append("Add each house separately and say whether it is self-occupied or let out. Two self-occupied houses can be shown at nil value; anything beyond that is deemed let out. ")
                } else {
                    append("Say whether the house is self-occupied or let out. ")
                }
                append("For a let-out house enter the gross rent, the rent you could not realise in the new field this year, and the municipal tax you actually paid during the year. Then 30% comes off automatically and the loan interest from your lender's certificate comes off after that. ")
                append("The return also wants your tenant's PAN where you deducted TDS under section 194-IB, or the tenant's TAN where they deducted under 194-I. Interest on a self-occupied house is capped at ₹2,00,000 and only under the old regime.")
            },
            documentIds = listOf(
                DocumentCatalog.homeLoanCertificate.id,
                DocumentCatalog.rentRecords.id
            )
        )
    }

    private fun capitalGains(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.CAPITAL_GAINS)) return null
        return FilingStep(
            id = "${prefix}_capital_gains",
            schedules = listOf(ItrSchedule.CAPITAL_GAINS),
            animation = TutorialAnimation.CAPITAL_GAINS,
            tip = "Schedule CG is filled section by section. Keep Schedule 112A open alongside it for the scrip-wise detail.",
            title = "Enter your capital gains",
            detail = "Schedule CG splits gains by asset and holding period, and this year there is no longer a split before and after 23 July 2024 — the whole year runs on one set of rates. Listed equity and equity funds: 20% short term under section 111A, 12.5% long term under 112A with the first ₹1,25,000 exempt, and the long term sales also go in scrip by scrip in Schedule 112A. Everything else long term is 12.5% without indexation, except that land or a building bought before 23 July 2024 can still be taxed at 20% with indexation if that works out lower for you. A property sale needs the sale value, the stamp duty value, the cost, the buyer's name, PAN and address, and any exemption under section 54, 54F or 54EC. The buyer's TDS shows up later under taxes paid.",
            documentIds = listOf(
                DocumentCatalog.brokerStatement.id,
                DocumentCatalog.mutualFundStatement.id,
                DocumentCatalog.propertySalePapers.id,
                DocumentCatalog.otherAssetProof.id
            )
        )
    }

    private fun cryptoAssets(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.VIRTUAL_DIGITAL_ASSETS)) return null
        return FilingStep(
            id = "${prefix}_vda",
            schedules = listOf(ItrSchedule.VIRTUAL_DIGITAL_ASSETS),
            animation = TutorialAnimation.CRYPTO,
            tip = "Schedule VDA takes one row per transfer. Download the exchange's tax report as a spreadsheet, it is far easier to copy from.",
            title = "Report your crypto trades",
            detail = "Schedule VDA wants every transfer with its date, cost and sale price — copy them from the exchange's tax report. Each trade is taxed flat at 30% with no set-off of losses, and the 1% the exchange already cut appears under taxes paid.",
            documentIds = listOf(DocumentCatalog.cryptoStatement.id)
        )
    }

    private fun otherIncome(prefix: String, answers: OnboardingAnswers) = FilingStep(
        id = "${prefix}_other_income",
        schedules = listOf(ItrSchedule.OTHER_SOURCES),
        animation = TutorialAnimation.OTHER_INCOME,
        tip = "Interest hides in four separate rows: savings, deposits, income tax refund and anything else. Filling only the first is the usual slip.",
        title = "Add interest and other income",
        detail = buildString {
            append("Enter savings and deposit interest from your bank's certificate, and check the dividends against your AIS. Small amounts still count — a mismatch here is the most common reason for a notice later. Dividends also need the quarterly break-up lower in the schedule, or interest under section 234C is added. Money a company paid you to buy back its shares is now taxed here as a deemed dividend under section 2(22)(f), with your cost treated as a capital loss. A family pension is entered here too, with a deduction of a third of it up to ₹25,000 under the new regime, ₹15,000 under the old.")
            if (answers.has(IncomeSource.WINNINGS)) {
                append(" Winnings from games, betting or lotteries go in here too, at a flat rate, copied from the platform's statement.")
            }
        },
        documentIds = listOf(
            DocumentCatalog.interestCertificate.id,
            DocumentCatalog.annualInformationStatement.id,
            DocumentCatalog.winningsCertificate.id
        )
    )

    private fun exemptIncome(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.AGRICULTURE)) return null
        return FilingStep(
            id = "${prefix}_exempt_income",
            schedules = listOf(ItrSchedule.EXEMPT),
            animation = TutorialAnimation.EXEMPT_INCOME,
            tip = "Schedule EI sits with the other schedules near the end, not under the income heads.",
            title = "Report exempt and farm income",
            detail = "Farm income stays exempt but goes into Schedule EI, worked out from your sale receipts and land records. Above ₹5,000 it still raises the rate charged on the rest of your income, so report it exactly.",
            documentIds = listOf(DocumentCatalog.agriculturalRecords.id)
        )
    }

    private fun broughtForwardLosses(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.CARRIED_FORWARD_LOSS)) return null
        return FilingStep(
            id = "${prefix}_losses",
            schedules = listOf(ItrSchedule.LOSSES),
            animation = TutorialAnimation.LOSSES,
            tip = "Schedule CFL is pre-filled from last year only if that return was filed on time. Otherwise it stays empty and the loss is gone.",
            title = "Bring forward last year's losses",
            detail = "Schedule CFL carries in the losses you reported earlier — copy each year's figure exactly from last year's return and computation. A loss only survives if the return that reported it was filed on time.",
            documentIds = listOf(DocumentCatalog.lastYearReturn.id)
        )
    }

    private fun foreignAssets(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS)) return null
        return FilingStep(
            id = "${prefix}_foreign_assets",
            schedules = listOf(ItrSchedule.FOREIGN),
            animation = TutorialAnimation.FOREIGN,
            tip = "Schedule FA runs on the calendar year, not the financial year. That catches nearly everyone the first time.",
            title = "Declare what you hold abroad",
            detail = "Schedule FA lists every foreign account, share and property by calendar year with its peak value — including employer RSUs sitting in a foreign brokerage. Leaving one out is treated seriously, so work through each statement.",
            documentIds = listOf(DocumentCatalog.foreignAssetDetails.id)
        )
    }

    private fun foreignTaxRelief(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF)) return null
        return FilingStep(
            id = "${prefix}_foreign_tax",
            schedules = listOf(ItrSchedule.FOREIGN),
            animation = TutorialAnimation.FOREIGN,
            tip = "Form 67 is under e-File, then Income Tax Forms. File it first, then come back to the return.",
            title = "Claim relief for foreign tax",
            detail = "File Form 67 on the portal before the return — the credit is refused without it. Then enter the foreign income in Schedule FSI and the tax already paid abroad in Schedule TR, from the withholding proof.",
            documentIds = listOf(DocumentCatalog.foreignTaxProof.id)
        )
    }

    private fun specialSchedules(prefix: String, answers: OnboardingAnswers): FilingStep? {
        val situations = answers.specialSituations.orEmpty()
        val relevant = situations.any {
            it == SpecialSituation.UNLISTED_SHARES ||
                it == SpecialSituation.COMPANY_DIRECTOR ||
                it == SpecialSituation.DEFERRED_ESOP_TAX ||
                it == SpecialSituation.CLUBBED_INCOME ||
                it == SpecialSituation.REPRESENTATIVE_ASSESSEE
        }
        if (!relevant && !answers.aboveOneCrore()) return null
        return FilingStep(
            id = "${prefix}_special_schedules",
            schedules = listOf(ItrSchedule.NO_ENTRY),
            animation = TutorialAnimation.EXTRA_SCHEDULES,
            tip = "These start as yes or no questions in the general information section, and each yes opens a table.",
            title = "Fill the remaining special schedules",
            detail = "The return asks separately about directorships, unlisted shares, deferred ESOP tax and income clubbed from family — answer from the documents you collected, and give the company's name, PAN and your DIN where it asks. Above ₹1 crore of total income, Schedule AL also wants your assets and liabilities as on 31 March; the trigger used to be ₹50 lakh and was raised this year.",
            documentIds = listOf(
                DocumentCatalog.unlistedShareDetails.id,
                DocumentCatalog.directorDetails.id,
                DocumentCatalog.esopPerquisiteStatement.id,
                DocumentCatalog.clubbedIncomeProof.id,
                DocumentCatalog.representativeAuthority.id
            )
        )
    }

    private fun deductions(prefix: String) = FilingStep(
        id = "${prefix}_deductions",
        schedules = listOf(ItrSchedule.DEDUCTIONS),
        animation = TutorialAnimation.DEDUCTIONS,
        tip = "Every section is a dropdown with its sub-clause now. Picking the wrong clause is the quickest way to a defective return notice.",
        title = "Claim your deductions",
        detail = "Pick each section from the dropdown and give the clause, which the form now insists on. Under the old regime this is where 80C, 80CCD(1B), 80D, 80DD, 80DDB, 80E, 80EE, 80EEA, 80EEB, 80G, 80GG, 80GGC, 80TTA, 80TTB and 80U go, each from the proof you collected. A donation under 80G or 80GGC needs the donee's PAN, the transaction reference number and the bank IFSC before the return will accept it. Under the new regime almost all of it falls away, but three survive and are worth checking: the standard deduction against salary, your employer's NPS contribution under 80CCD(2) up to 14% of basic and dearness allowance, and 80CCH for the Agnipath scheme.",
        documentIds = listOf(DocumentCatalog.deductionProofs.id)
    )

    private fun taxesPaid(prefix: String, hasBusinessIncome: Boolean) = FilingStep(
        id = "${prefix}_taxes_paid",
        schedules = listOf(ItrSchedule.TAXES_PAID),
        animation = TutorialAnimation.MATCH_TAXES,
        tip = "The TDS schedules come pre-filled from Form 26AS, and anything you add by hand is matched against it afterwards.",
        title = "Match the tax already paid",
        detail = buildString {
            append("The Taxes Paid screen lists every TDS entry against your PAN, and this year each row also carries the section it was deducted under. Check each one against your Form 26AS — if something your employer or bank cut is missing here, chase them before you submit, or the credit is lost. Tax collected at source, on a foreign remittance or a car, is claimed in the TCS schedule from the collector's Form 27D. If AIS shows something that is not yours, send feedback on it from the AIS screen rather than quietly ignoring it.")
            if (hasBusinessIncome) {
                append(" Cross-check the Form 16A certificates from your clients too, and add every advance tax and self assessment challan with its BSR code, serial number and date. If you are a partner, the firm has had to deduct 10% under the new section 194T on remuneration and interest above ₹20,000 since 1 April 2025, so that credit should be sitting in Form 26AS as well.")
            }
        },
        documentIds = buildList {
            add(DocumentCatalog.form26as.id)
            add(DocumentCatalog.annualInformationStatement.id)
            if (hasBusinessIncome) add(DocumentCatalog.form16a.id)
        }
    )

    private fun payTax(prefix: String) = FilingStep(
        id = "${prefix}_pay_tax",
        animation = TutorialAnimation.PAY_TAX,
        tip = "e-Pay Tax is its own menu. Pay there first, then come back and the challan details go into Schedule IT.",
        title = "Pay whatever is still due",
        detail = "Advance tax was due in four instalments during the year, 15% by 15 June, 45% by 15 September, 75% by 15 December and all of it by 15 March, and a resident senior citizen with no business income is excused from it altogether. The computation now shows tax payable or refundable. Anything still payable is self assessment tax: pay it from e-Pay Tax on the portal, challan type 300, against AY ${TaxYear.ASSESSMENT_YEAR}, before you submit. Interest under sections 234B and 234C is already added if your advance tax fell short during the year, and section 234A adds 1% a month for every month after the due date. Then copy the BSR code, challan serial number, date and amount into Schedule IT so the payment is matched."
    )

    private fun deadlines(prefix: String, form: ItrForm) = FilingStep(
        id = "${prefix}_deadlines",
        animation = TutorialAnimation.CALENDAR,
        tip = "The filing screen shows the due date. Once it has passed, the same screen quietly files a belated return instead.",
        title = "The dates that decide what this costs",
        detail = buildString {
            val dueDate = when (form) {
                ItrForm.ITR1, ItrForm.ITR2 -> TaxYear.DUE_DATE_WITHOUT_BUSINESS
                ItrForm.ITR3, ItrForm.ITR4 -> TaxYear.DUE_DATE_BUSINESS_WITHOUT_AUDIT
            }
            append("For AY ${TaxYear.ASSESSMENT_YEAR} your due date is $dueDate")
            if (form == ItrForm.ITR3 || form == ItrForm.ITR4) {
                append(", or ${TaxYear.DUE_DATE_WITH_AUDIT} if your accounts need an audit, with the audit report itself due by ${TaxYear.AUDIT_REPORT_DUE_DATE}")
            }
            append(". ")
            append("Past that you can still file a belated return until ${TaxYear.BELATED_RETURN_DATE}, but it costs a fee under section 234F of ₹5,000, or ₹1,000 if your total income is ₹5 lakh or less, plus 1% a month under section 234A on unpaid tax, and you lose the right to carry forward business and capital losses — only a house property loss survives. ")
            append("A return already filed can be revised until ${TaxYear.REVISED_RETURN_DATE}. After that the only way back in is an updated return under section 139(8A), open for 48 months until ${TaxYear.UPDATED_RETURN_DATE}, with 25% to 70% extra tax depending on how late it is. ")
            append("Whatever you file has to be e-verified within 30 days or it counts as never filed.")
        }
    )

    private fun previewSubmit(prefix: String) = FilingStep(
        id = "${prefix}_preview_submit",
        schedules = listOf(ItrSchedule.COMPUTED),
        animation = TutorialAnimation.SUBMIT,
        tip = "Validate flags errors schedule by schedule. Fix them there, or the submit button never opens.",
        title = "Preview, validate and submit",
        detail = "The portal shows the final computation — tax due or refund. Pick the validated bank account the refund should land in, run Validate to catch errors, then Proceed to Verification and submit.",
        documentIds = listOf(DocumentCatalog.bankAccount.id)
    )

    private fun everify(prefix: String) = FilingStep(
        id = "${prefix}_everify",
        animation = TutorialAnimation.VERIFY,
        tip = "Aadhaar OTP is the quickest. Net banking or a bank EVC works too if your mobile is not linked to Aadhaar.",
        title = "E-verify within 30 days",
        detail = "The return does not count until it is verified. The quickest way is an OTP on the mobile linked to your Aadhaar. Do it right after submitting — after 30 days the return is treated as never filed.",
        documentIds = listOf(DocumentCatalog.aadhaar.id)
    )
}