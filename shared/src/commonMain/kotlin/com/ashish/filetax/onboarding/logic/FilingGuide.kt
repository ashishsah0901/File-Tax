package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.FilingStep
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TaxYear

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
        add(login("itr1"))
        add(startReturn("itr1", ItrForm.ITR1))
        add(regime("itr1", hasBusinessIncome = false))
        salary("itr1", answers)?.let(::add)
        houseProperty("itr1", answers)?.let(::add)
        add(otherIncome("itr1", answers))
        add(deductions("itr1"))
        add(taxesPaid("itr1", hasBusinessIncome = false))
        add(previewSubmit("itr1"))
        add(everify("itr1"))
    }

    private fun itr2Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(login("itr2"))
        add(startReturn("itr2", ItrForm.ITR2))
        add(regime("itr2", hasBusinessIncome = false))
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
        add(previewSubmit("itr2"))
        add(everify("itr2"))
    }

    private fun itr3Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(login("itr3"))
        add(startReturn("itr3", ItrForm.ITR3))
        add(regime("itr3", hasBusinessIncome = true))
        add(
            FilingStep(
                id = "itr3_business_books",
                title = "Enter the business income from your books",
                detail = "Schedule BP wants the year's figures from your profit and loss account and balance sheet, line by line. Keep the turnover consistent with your GST returns — the department matches the two. If your accounts need an audit, that report has to be filed first.",
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
                    title = "Report your trading as business income",
                    detail = "Intraday trades are speculative business and futures and options are normal business — the two go into separate parts of Schedule BP. Work the turnover and profit off your broker's statement, not memory.",
                    documentIds = listOf(DocumentCatalog.brokerStatement.id)
                )
            )
        }
        if (answers.has(BusinessKind.PARTNER_IN_FIRM)) {
            add(
                FilingStep(
                    id = "itr3_partner",
                    title = "Enter your share from the firm",
                    detail = "The firm's name, PAN and your profit share go into their own schedule. Your share of profit is exempt, but the salary and interest the firm paid you are taxable — split them exactly as the firm's statement does.",
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
        add(previewSubmit("itr3"))
        add(everify("itr3"))
    }

    private fun itr4Steps(answers: OnboardingAnswers): List<FilingStep> = buildList {
        add(login("itr4"))
        add(startReturn("itr4", ItrForm.ITR4))
        add(regime("itr4", hasBusinessIncome = true))
        add(
            FilingStep(
                id = "itr4_presumptive",
                title = "Declare income at the presumptive rate",
                detail = "Enter the year's turnover split into digital and cash receipts. Business income is presumed at 6% of the digital share and 8% of the cash share; professional fees at 50%. You can declare more than that, never less, and the turnover should match your GST returns if you are registered.",
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
        add(previewSubmit("itr4"))
        add(everify("itr4"))
    }

    private fun login(prefix: String) = FilingStep(
        id = "${prefix}_login",
        title = "Log in to the portal",
        detail = "Go to incometax.gov.in and log in with your PAN as the user ID. While you are there, open Profile and check that your bank account shows as validated — a refund cannot arrive without it.",
        documentIds = listOf(DocumentCatalog.pan.id, DocumentCatalog.bankAccount.id)
    )

    private fun startReturn(prefix: String, form: ItrForm) = FilingStep(
        id = "${prefix}_start_return",
        title = "Start the return",
        detail = "Go to e-File, then Income Tax Returns, then File Income Tax Return. Pick AY ${TaxYear.ASSESSMENT_YEAR}, Online mode, Individual, and ${form.label} when it asks which form."
    )

    private fun regime(prefix: String, hasBusinessIncome: Boolean) = FilingStep(
        id = "${prefix}_regime",
        title = "Confirm your details and pick a regime",
        detail = buildString {
            append("Check the pre-filled name, address, email and mobile. Then choose between the new and old tax regime — the new one is the default, and the old one only wins if your deductions are large enough to beat its lower rates.")
            if (hasBusinessIncome) {
                append(" With business income there is one extra catch: opting out of the new regime needs Form 10-IEA filed before the return, and the choice sticks for later years.")
            }
        },
        documentIds = listOf(DocumentCatalog.deductionProofs.id)
    )

    private fun salary(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.SALARY_PENSION)) return null
        return FilingStep(
            id = "${prefix}_salary",
            title = "Check the pre-filled salary",
            detail = "The salary schedule comes pre-filled from what your employer reported. Match it line by line against Part B of your Form 16 — gross salary, exempt allowances and the standard deduction — and fix anything that differs. Changed jobs? Add every employer.",
            documentIds = listOf(DocumentCatalog.form16.id)
        )
    }

    private fun houseProperty(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(IncomeSource.HOUSE_PROPERTY)) return null
        val multiple = answers.housePropertyCount == HousePropertyCount.MORE_THAN_ONE
        return FilingStep(
            id = "${prefix}_house_property",
            title = if (multiple) "Fill in each house property" else "Fill in your house property",
            detail = if (multiple) {
                "Add each house separately and say whether it is self-occupied or let out. For the let-out ones, enter the rent received and the municipal tax you paid, and enter the loan interest from each lender's certificate."
            } else {
                "Say whether the house is self-occupied or let out. If let out, enter the rent received and the municipal tax you paid. Enter the home loan interest from your certificate — it comes off this income."
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
            title = "Enter your capital gains",
            detail = "Schedule CG splits gains by asset and holding period. Copy the short-term and long-term totals from your broker and fund statements — they arrive already worked out. A property sale needs the dates and figures from the deeds, and the buyer's TDS shows up later under taxes paid.",
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
            title = "Report your crypto trades",
            detail = "Schedule VDA wants every transfer with its date, cost and sale price — copy them from the exchange's tax report. Each trade is taxed flat at 30% with no set-off of losses, and the 1% the exchange already cut appears under taxes paid.",
            documentIds = listOf(DocumentCatalog.cryptoStatement.id)
        )
    }

    private fun otherIncome(prefix: String, answers: OnboardingAnswers) = FilingStep(
        id = "${prefix}_other_income",
        title = "Add interest and other income",
        detail = buildString {
            append("Enter savings and deposit interest from your bank's certificate, and check the dividends against your AIS. Small amounts still count — a mismatch here is the most common reason for a notice later.")
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
            title = "Report exempt and farm income",
            detail = "Farm income stays exempt but goes into Schedule EI, worked out from your sale receipts and land records. Above ₹5,000 it still raises the rate charged on the rest of your income, so report it exactly.",
            documentIds = listOf(DocumentCatalog.agriculturalRecords.id)
        )
    }

    private fun broughtForwardLosses(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.CARRIED_FORWARD_LOSS)) return null
        return FilingStep(
            id = "${prefix}_losses",
            title = "Bring forward last year's losses",
            detail = "Schedule CFL carries in the losses you reported earlier — copy each year's figure exactly from last year's return and computation. A loss only survives if the return that reported it was filed on time.",
            documentIds = listOf(DocumentCatalog.lastYearReturn.id)
        )
    }

    private fun foreignAssets(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS)) return null
        return FilingStep(
            id = "${prefix}_foreign_assets",
            title = "Declare what you hold abroad",
            detail = "Schedule FA lists every foreign account, share and property by calendar year with its peak value — including employer RSUs sitting in a foreign brokerage. Leaving one out is treated seriously, so work through each statement.",
            documentIds = listOf(DocumentCatalog.foreignAssetDetails.id)
        )
    }

    private fun foreignTaxRelief(prefix: String, answers: OnboardingAnswers): FilingStep? {
        if (!answers.has(SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF)) return null
        return FilingStep(
            id = "${prefix}_foreign_tax",
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
        if (!relevant && answers.totalIncomeAboveFiftyLakh != true) return null
        return FilingStep(
            id = "${prefix}_special_schedules",
            title = "Fill the remaining special schedules",
            detail = "The return asks separately about directorships, unlisted shares, deferred ESOP tax and income clubbed from family — answer from the documents you collected. Above ₹50 lakh, Schedule AL also wants your assets and liabilities as on 31 March.",
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
        title = "Claim your deductions",
        detail = "Only if you picked the old regime: enter your 80C investments, health insurance, education loan interest and donations from the proofs you collected. Under the new regime this screen mostly stays empty.",
        documentIds = listOf(DocumentCatalog.deductionProofs.id)
    )

    private fun taxesPaid(prefix: String, hasBusinessIncome: Boolean) = FilingStep(
        id = "${prefix}_taxes_paid",
        title = "Match the tax already paid",
        detail = buildString {
            append("The Taxes Paid screen lists every TDS entry against your PAN. Check each one against your Form 26AS — if something your employer or bank cut is missing here, chase them before you submit, or the credit is lost.")
            if (hasBusinessIncome) {
                append(" Cross-check the Form 16A certificates from your clients too, and add any advance tax you paid during the year.")
            }
        },
        documentIds = buildList {
            add(DocumentCatalog.form26as.id)
            add(DocumentCatalog.annualInformationStatement.id)
            if (hasBusinessIncome) add(DocumentCatalog.form16a.id)
        }
    )

    private fun previewSubmit(prefix: String) = FilingStep(
        id = "${prefix}_preview_submit",
        title = "Preview, validate and submit",
        detail = "The portal shows the final computation — tax due or refund. Pick the validated bank account the refund should land in, run Validate to catch errors, then Proceed to Verification and submit.",
        documentIds = listOf(DocumentCatalog.bankAccount.id)
    )

    private fun everify(prefix: String) = FilingStep(
        id = "${prefix}_everify",
        title = "E-verify within 30 days",
        detail = "The return does not count until it is verified. The quickest way is an OTP on the mobile linked to your Aadhaar. Do it right after submitting — after 30 days the return is treated as never filed.",
        documentIds = listOf(DocumentCatalog.aadhaar.id)
    )
}
