package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.onboarding.logic.OnboardingFlow
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand

data class DocumentFinding(
    val label: String,
    val evidence: String
)

data class InferredAnswers(
    val answers: OnboardingAnswers,
    val findings: List<DocumentFinding>,
    val estimatedTotalIncome: Long?,
    val stillToAnswer: List<String>
)

object AnswersFromDocuments {

    private const val FIFTY_LAKH = 5_000_000L
    private const val ONE_CRORE = 10_000_000L

    fun infer(documents: List<ParsedDocument>, base: OnboardingAnswers = OnboardingAnswers()): InferredAnswers {
        if (documents.isEmpty()) {
            return InferredAnswers(base, emptyList(), null, stillToAnswer(base))
        }

        val findings = mutableListOf<DocumentFinding>()
        val sources = base.incomeSources.orEmpty().toMutableSet()
        val situations = base.specialSituations.orEmpty().toMutableSet()
        val gainKinds = base.capitalGainKinds.orEmpty().toMutableSet()
        val businessKinds = base.businessKinds.orEmpty().toMutableSet()

        fun evidenceFor(
            label: String,
            kinds: List<DocumentKind> = emptyList(),
            figures: List<FigureKind> = emptyList()
        ): String? {
            val byKind = documents.firstOrNull { it.kind in kinds }
            val byFigure = documents.firstOrNull { document -> figures.any { document.amountOf(it) != null } }
            val document = byKind ?: byFigure ?: return null
            findings += DocumentFinding(label = label, evidence = "${document.kind.label} · ${document.fileName}")
            return document.fileName
        }

        evidenceFor(
            label = "Salary or pension",
            kinds = listOf(
                DocumentKind.FORM_16,
                DocumentKind.FORM_12BA,
                DocumentKind.FULL_AND_FINAL_SETTLEMENT,
                DocumentKind.FORM_10E,
                DocumentKind.SALARY_SLIP,
                DocumentKind.ESOP_PERQUISITE_STATEMENT
            ),
            figures = listOf(FigureKind.GROSS_SALARY, FigureKind.INCOME_CHARGEABLE_UNDER_SALARY)
        )?.let { sources += IncomeSource.SALARY_PENSION }

        evidenceFor(
            label = "A house or flat",
            kinds = listOf(DocumentKind.HOME_LOAN_CERTIFICATE, DocumentKind.RENT_RECORDS),
            figures = listOf(FigureKind.RENT_RECEIVED, FigureKind.HOME_LOAN_INTEREST_PAID, FigureKind.HOUSE_PROPERTY_INCOME)
        )?.let { sources += IncomeSource.HOUSE_PROPERTY }

        evidenceFor(
            label = "Interest or dividends",
            kinds = listOf(DocumentKind.INTEREST_CERTIFICATE),
            figures = listOf(
                FigureKind.SAVINGS_INTEREST,
                FigureKind.DEPOSIT_INTEREST,
                FigureKind.OTHER_INTEREST,
                FigureKind.DIVIDEND
            )
        )?.let { sources += IncomeSource.INTEREST_DIVIDEND_OTHER }

        evidenceFor(
            label = "Something you sold",
            kinds = listOf(
                DocumentKind.CAPITAL_GAINS_STATEMENT,
                DocumentKind.MUTUAL_FUND_STATEMENT,
                DocumentKind.PROPERTY_SALE_PAPERS,
                DocumentKind.OTHER_ASSET_PROOF
            ),
            figures = listOf(
                FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY,
                FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY,
                FigureKind.SALE_OF_SECURITIES,
                FigureKind.SALE_OF_IMMOVABLE_PROPERTY
            )
        )?.let { sources += IncomeSource.CAPITAL_GAINS }

        evidenceFor(
            label = "Crypto or other virtual digital assets",
            kinds = listOf(DocumentKind.CRYPTO_STATEMENT),
            figures = listOf(FigureKind.VDA_SALE_CONSIDERATION, FigureKind.VDA_INCOME)
        )?.let { sources += IncomeSource.VIRTUAL_DIGITAL_ASSETS }

        evidenceFor(
            label = "Work of your own",
            kinds = listOf(
                DocumentKind.PROFIT_AND_LOSS,
                DocumentKind.BALANCE_SHEET,
                DocumentKind.TURNOVER_SUMMARY,
                DocumentKind.GST_RETURN,
                DocumentKind.BUSINESS_BANK_STATEMENT,
                DocumentKind.FIRM_SHARE_STATEMENT,
                DocumentKind.TRADING_PROFIT_AND_LOSS,
                DocumentKind.TAX_AUDIT_REPORT
            ),
            figures = listOf(FigureKind.BUSINESS_RECEIPTS, FigureKind.BUSINESS_NET_PROFIT, FigureKind.GST_TURNOVER)
        )?.let { sources += IncomeSource.BUSINESS_PROFESSION }

        evidenceFor(
            label = "Winnings",
            kinds = listOf(DocumentKind.WINNINGS_CERTIFICATE),
            figures = listOf(FigureKind.WINNINGS)
        )?.let { sources += IncomeSource.WINNINGS }

        val agricultural = documents.firstNotNullOfOrNull { it.amountOf(FigureKind.AGRICULTURAL_INCOME) }
        evidenceFor(
            label = "Farming",
            kinds = listOf(DocumentKind.AGRICULTURAL_RECORDS),
            figures = listOf(FigureKind.AGRICULTURAL_INCOME)
        )?.let { sources += IncomeSource.AGRICULTURE }

        if (IncomeSource.CAPITAL_GAINS in sources) {
            if (documents.any {
                    it.amountOf(FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY) != null ||
                        it.amountOf(FigureKind.SALE_OF_SECURITIES) != null
                }
            ) {
                gainKinds += CapitalGainKind.LISTED_EQUITY_LONG_TERM
            }
            if (documents.any { it.amountOf(FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY) != null }) {
                gainKinds += CapitalGainKind.LISTED_EQUITY_SHORT_TERM
            }
            if (documents.any {
                    it.kind == DocumentKind.PROPERTY_SALE_PAPERS ||
                        it.amountOf(FigureKind.SALE_OF_IMMOVABLE_PROPERTY) != null
                }
            ) {
                gainKinds += CapitalGainKind.LAND_OR_BUILDING
            }
            if (documents.any { it.kind == DocumentKind.OTHER_ASSET_PROOF }) {
                gainKinds += CapitalGainKind.OTHER_ASSETS
            }
        }

        if (IncomeSource.BUSINESS_PROFESSION in sources) {
            if (documents.any { it.kind == DocumentKind.FIRM_SHARE_STATEMENT }) {
                businessKinds += BusinessKind.PARTNER_IN_FIRM
            }
            if (documents.any {
                    it.amountOf(FigureKind.SPECULATIVE_BUSINESS_INCOME) != null ||
                        it.amountOf(FigureKind.DERIVATIVES_INCOME) != null
                }
            ) {
                businessKinds += BusinessKind.SPECULATIVE_OR_DERIVATIVES
            }
            if (documents.any {
                    it.kind == DocumentKind.PROFIT_AND_LOSS ||
                        it.kind == DocumentKind.BALANCE_SHEET ||
                        it.kind == DocumentKind.TAX_AUDIT_REPORT
                }
            ) {
                businessKinds += BusinessKind.REGULAR_BOOKS
            }
        }

        if (documents.any { it.kind == DocumentKind.FOREIGN_ASSET_DETAILS || it.amountOf(FigureKind.FOREIGN_ASSET_VALUE) != null }) {
            situations += SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS
            findings += DocumentFinding("Something held outside India", "Schedule FA will be needed")
        }
        if (documents.any { it.kind == DocumentKind.FOREIGN_TAX_PROOF || it.amountOf(FigureKind.FOREIGN_TAX_PAID) != null }) {
            situations += SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF
            findings += DocumentFinding("Tax paid abroad", "Form 67, Schedule FSI and Schedule TR")
        }
        if (documents.any { it.amountOf(FigureKind.CARRIED_FORWARD_LOSS) != null }) {
            situations += SpecialSituation.CARRIED_FORWARD_LOSS
            findings += DocumentFinding("A loss carried forward", "Last year's return")
        }
        if (documents.any { it.kind == DocumentKind.UNLISTED_SHARE_DETAILS }) {
            situations += SpecialSituation.UNLISTED_SHARES
            findings += DocumentFinding("Unlisted shares held", "Unlisted share details")
        }
        if (documents.any { it.kind == DocumentKind.DIRECTOR_DETAILS }) {
            situations += SpecialSituation.COMPANY_DIRECTOR
            findings += DocumentFinding("A directorship", "Company directorship details")
        }
        if (documents.any { it.kind == DocumentKind.CLUBBED_INCOME_PROOF }) {
            situations += SpecialSituation.CLUBBED_INCOME
            findings += DocumentFinding("Income to be clubbed", "Proof of income to be clubbed")
        }
        if (documents.any { it.kind == DocumentKind.REPRESENTATIVE_AUTHORITY }) {
            situations += SpecialSituation.REPRESENTATIVE_ASSESSEE
            findings += DocumentFinding("Filing for someone else", "Authority to file as a representative")
        }

        val estimate = estimateTotalIncome(documents)
        var answers = base.copy(
            incomeSources = sources.takeIf { it.isNotEmpty() } ?: base.incomeSources,
            capitalGainKinds = gainKinds.takeIf { it.isNotEmpty() } ?: base.capitalGainKinds,
            businessKinds = businessKinds.takeIf { it.isNotEmpty() } ?: base.businessKinds,
            specialSituations = situations.takeIf { it.isNotEmpty() } ?: base.specialSituations
        )

        val longTermGain = documents.firstNotNullOfOrNull { it.amountOf(FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY) }
        if (longTermGain != null && answers.capitalGainKinds == setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)) {
            answers = answers.copy(equityLongTermGainWithinExemptLimit = longTermGain <= 125_000L)
        }
        if (agricultural != null) {
            answers = answers.copy(agriculturalIncomeAboveLimit = agricultural > 5_000L)
        }
        bandFor(estimate)?.let { answers = answers.copy(totalIncomeBand = it) }

        return InferredAnswers(
            answers = answers,
            findings = findings,
            estimatedTotalIncome = estimate,
            stillToAnswer = stillToAnswer(answers)
        )
    }

    private fun bandFor(estimate: Long?): TotalIncomeBand? = when {
        estimate == null -> null
        estimate < FIFTY_LAKH * 9 / 10 -> TotalIncomeBand.UP_TO_FIFTY_LAKH
        estimate in (FIFTY_LAKH * 11 / 10)..(ONE_CRORE * 9 / 10) -> TotalIncomeBand.FIFTY_LAKH_TO_ONE_CRORE
        estimate > ONE_CRORE * 11 / 10 -> TotalIncomeBand.ABOVE_ONE_CRORE
        else -> null
    }

    private fun estimateTotalIncome(documents: List<ParsedDocument>): Long? {
        val entries = ScanReportBuilder.build(documents, emptyList())
            .sections
            .flatMap { it.entries }
            .associate { it.figure.kind to it.figure.amount }
        if (entries.isEmpty()) return null

        val salary = entries[FigureKind.INCOME_CHARGEABLE_UNDER_SALARY] ?: entries[FigureKind.GROSS_SALARY]
        val houseProperty = entries[FigureKind.HOUSE_PROPERTY_INCOME]
            ?: entries[FigureKind.RENT_RECEIVED]?.let { it * 7 / 10 }
        val counted = listOfNotNull(
            salary,
            houseProperty,
            entries[FigureKind.SAVINGS_INTEREST],
            entries[FigureKind.DEPOSIT_INTEREST],
            entries[FigureKind.OTHER_INTEREST],
            entries[FigureKind.DIVIDEND],
            entries[FigureKind.BUYBACK_PROCEEDS],
            entries[FigureKind.FAMILY_PENSION],
            entries[FigureKind.WINNINGS],
            entries[FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY],
            entries[FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY],
            entries[FigureKind.VDA_INCOME],
            entries[FigureKind.BUSINESS_NET_PROFIT],
            entries[FigureKind.SPECULATIVE_BUSINESS_INCOME],
            entries[FigureKind.DERIVATIVES_INCOME],
            entries[FigureKind.PARTNER_REMUNERATION],
            entries[FigureKind.PARTNER_INTEREST_ON_CAPITAL],
            entries[FigureKind.FOREIGN_INCOME]
        )
        if (counted.isEmpty()) return null
        return counted.sum().coerceAtLeast(0L)
    }

    private fun stillToAnswer(answers: OnboardingAnswers): List<String> =
        OnboardingFlow.questions(answers)
            .filterNot { OnboardingFlow.isAnswered(answers, it.id) }
            .map { it.title }
}