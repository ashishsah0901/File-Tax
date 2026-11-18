package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.ItrRecommendation
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TaxYear

object ItrFormDeterminer {

    fun determine(answers: OnboardingAnswers): ItrRecommendation {
        val simpleFormBlockers = simpleFormBlockers(answers)
        if (!answers.has(IncomeSource.BUSINESS_PROFESSION)) {
            return if (simpleFormBlockers.isEmpty()) {
                ItrRecommendation(
                    form = ItrForm.ITR1,
                    reasons = listOf("Nothing in your answers goes past what ITR-1 covers: a resident earning up to ₹50 lakh from salary or pension, up to two houses, ordinary interest income, and a long term equity gain no larger than ₹1.25 lakh."),
                    cautions = cautions(answers, ItrForm.ITR1)
                )
            } else {
                ItrRecommendation(
                    form = ItrForm.ITR2,
                    reasons = simpleFormBlockers,
                    cautions = cautions(answers, ItrForm.ITR2)
                )
            }
        }

        val fullReturnReasons = fullReturnReasons(answers)
        if (fullReturnReasons.isEmpty() && simpleFormBlockers.isEmpty()) {
            return ItrRecommendation(
                form = ItrForm.ITR4,
                reasons = listOf("You are declaring business or professional income at a presumptive rate, and the rest of your income stays inside the Sugam limits."),
                alternative = "ITR-3 is also open to you if you would rather declare your actual profit from books than a flat percentage of turnover.",
                cautions = cautions(answers, ItrForm.ITR4)
            )
        }

        return ItrRecommendation(
            form = ItrForm.ITR3,
            reasons = buildList {
                if (fullReturnReasons.isEmpty()) {
                    add("You have business or professional income, so your return is either ITR-3 or ITR-4.")
                }
                addAll(fullReturnReasons)
                addAll(simpleFormBlockers)
            },
            cautions = cautions(answers, ItrForm.ITR3)
        )
    }

    private fun cautions(answers: OnboardingAnswers, form: ItrForm): List<String> = buildList {
        val shortForm = form == ItrForm.ITR1 || form == ItrForm.ITR4
        if (answers.residentialStatus == ResidentialStatus.NON_RESIDENT) {
            add("As a non-resident the rebate under section 87A is not available to you, the basic exemption cannot be set against gains taxed at the special rates, and a treaty rate needs a tax residency certificate and Form 10F. Only income that arose in India, or was received here, is taxed.")
        }
        if (shortForm && answers.has(SpecialSituation.CLUBBED_INCOME)) {
            add("Income clubbed from your spouse or minor child fits the short forms only if it is itself salary, house property or ordinary other-sources income. Anything else, such as a capital gain in their name, moves you to ITR-2.")
        }
        if (answers.has(SpecialSituation.REPRESENTATIVE_ASSESSEE)) {
            add("Filing for someone else no longer pushes you to a longer form. From AY ${TaxYear.ASSESSMENT_YEAR} every form, ITR-1 included, asks only for your name, phone number and email as the representative.")
        }
        if (shortForm && answers.housePropertyCount == HousePropertyCount.TWO) {
            add("Two houses fit in ITR-1 and ITR-4 only from AY ${TaxYear.ASSESSMENT_YEAR}. A third one, at any time in the year, moves you to ITR-2.")
        }
        if (form == ItrForm.ITR4 && answers.hasPresumptiveBusiness()) {
            add("Declare at least 6% of what came through banking channels, 8% of what came in cash, or 50% of professional fees. Declare less than that with income above the basic exemption and you need books and an audit, and section 44AD then locks you out of the presumptive route for five years.")
        }
        if (answers.aboveOneCrore()) {
            add("Above ₹1 crore of total income the return also wants Schedule AL: everything you own and everything you owe as on 31 March. The old ₹50 lakh trigger was raised this year.")
        }
    }

    private fun fullReturnReasons(answers: OnboardingAnswers): List<String> {
        val kinds = answers.businessKinds.orEmpty()
        val reasons = mutableListOf<String>()
        if (BusinessKind.REGULAR_BOOKS in kinds) {
            reasons += "You are declaring actual profit from your books, which needs the full business schedules in ITR-3."
        }
        if (BusinessKind.PARTNER_IN_FIRM in kinds) {
            reasons += "A share of profit or salary from a partnership firm can only be reported in ITR-3."
        }
        if (BusinessKind.SPECULATIVE_OR_DERIVATIVES in kinds) {
            reasons += "Intraday and F&O results are business income with their own schedules, so ITR-3 is the safe form."
        }
        if (answers.presumptiveTurnoverAboveLimit == true) {
            reasons += "Your turnover crossed the presumptive ceiling, so the flat percentage is off the table this year and the real profit goes through the full business schedules."
        }
        if (kinds.isEmpty()) {
            reasons += "You have business or professional income, and until you pick how to report it, ITR-3 is the form that always works."
        }
        return reasons
    }

    private fun simpleFormBlockers(answers: OnboardingAnswers): List<String> {
        val blockers = mutableListOf<String>()

        when (answers.residentialStatus) {
            ResidentialStatus.NON_RESIDENT ->
                blockers += "You were a non-resident this year, and the short forms are only for residents."

            ResidentialStatus.NOT_ORDINARILY_RESIDENT ->
                blockers += "Being resident but not ordinarily resident keeps you off the short forms."

            else -> Unit
        }

        if (answers.aboveFiftyLakh()) {
            blockers += "Your total income is above ₹50 lakh, which is the ceiling for the short forms."
        }

        if (answers.housePropertyCount == HousePropertyCount.MORE_THAN_TWO) {
            blockers += "You own more than two houses, and even after this year's relaxation the short forms have room for only two."
        }

        blockers += capitalGainsBlocker(answers)

        if (answers.has(IncomeSource.VIRTUAL_DIGITAL_ASSETS)) {
            blockers += "Crypto and other virtual digital assets are reported in Schedule VDA, which the short forms do not carry."
        }

        if (answers.has(IncomeSource.WINNINGS)) {
            blockers += "Lottery, betting and gaming winnings are taxed at a special rate that the short forms cannot handle."
        }

        if (answers.agriculturalIncomeAboveLimit == true) {
            blockers += "Agricultural income above ₹5,000 has to be reported on a longer form."
        }

        answers.specialSituations.orEmpty().forEach { situation ->
            situationBlocker(situation)?.let { blockers += it }
        }

        return blockers
    }

    private fun capitalGainsBlocker(answers: OnboardingAnswers): List<String> {
        if (!answers.has(IncomeSource.CAPITAL_GAINS)) return emptyList()
        val kinds = answers.capitalGainKinds.orEmpty()
        val equityLongTermOnly = kinds == setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)
        if (equityLongTermOnly && answers.equityLongTermGainWithinExemptLimit == true) return emptyList()
        if (equityLongTermOnly) {
            return listOf("Your long-term equity gain crosses ₹1.25 lakh, so it needs the capital gains schedule.")
        }
        return listOf("What you sold has to go through the capital gains schedule, which the short forms leave out.")
    }

    private fun situationBlocker(situation: SpecialSituation): String? = when (situation) {
        SpecialSituation.COMPANY_DIRECTOR ->
            "A director in a company cannot use the short forms, whatever the income."

        SpecialSituation.UNLISTED_SHARES ->
            "Holding unlisted shares means declaring them in a schedule the short forms do not have."

        SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS ->
            "Assets or accounts outside India go in Schedule FA, and skipping it is treated seriously."

        SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF ->
            "Foreign income and the tax credit for it need schedules the short forms leave out."

        SpecialSituation.CARRIED_FORWARD_LOSS ->
            "Carrying losses forward, or setting off old ones, needs the loss schedules."

        SpecialSituation.DEFERRED_ESOP_TAX ->
            "Deferred ESOP tax from an eligible startup has its own schedule outside the short forms."

        SpecialSituation.FOREIGN_RETIREMENT_ACCOUNT ->
            "Income from a retirement account held abroad is reported under section 89A, and from AY ${TaxYear.ASSESSMENT_YEAR} that reporting was taken out of the short forms."

        SpecialSituation.INCOME_APPORTIONED_UNDER_5A ->
            "Income split with a spouse under the Portuguese Civil Code goes through section 5A, which the short forms cannot carry."

        SpecialSituation.UNEXPLAINED_INCOME_AT_SPECIAL_RATE ->
            "Income taxed at the flat rate under section 115BBE has to be shown separately, and the short forms have no room for it."

        SpecialSituation.CLUBBED_INCOME -> null

        SpecialSituation.REPRESENTATIVE_ASSESSEE -> null

        SpecialSituation.TDS_UNDER_194N ->
            "Tax deducted on cash withdrawals under section 194N cannot be claimed on the short forms."
    }
}