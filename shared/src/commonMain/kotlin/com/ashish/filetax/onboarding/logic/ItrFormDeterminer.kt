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

object ItrFormDeterminer {

    fun determine(answers: OnboardingAnswers): ItrRecommendation {
        val simpleFormBlockers = simpleFormBlockers(answers)
        if (!answers.has(IncomeSource.BUSINESS_PROFESSION)) {
            return if (simpleFormBlockers.isEmpty()) {
                ItrRecommendation(
                    form = ItrForm.ITR1,
                    reasons = listOf("Nothing in your answers goes past what ITR-1 covers: a resident earning up to ₹50 lakh from salary or pension, one house and ordinary interest income.")
                )
            } else {
                ItrRecommendation(form = ItrForm.ITR2, reasons = simpleFormBlockers)
            }
        }

        val fullReturnReasons = fullReturnReasons(answers)
        if (fullReturnReasons.isEmpty() && simpleFormBlockers.isEmpty()) {
            return ItrRecommendation(
                form = ItrForm.ITR4,
                reasons = listOf("You are declaring business or professional income at a presumptive rate, and the rest of your income stays inside the Sugam limits."),
                alternative = "ITR-3 is also open to you if you would rather declare your actual profit from books than a flat percentage of turnover."
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
            }
        )
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

        if (answers.totalIncomeAboveFiftyLakh == true) {
            blockers += "Your total income is above ₹50 lakh, which is the ceiling for the short forms."
        }

        if (answers.housePropertyCount == HousePropertyCount.MORE_THAN_ONE) {
            blockers += "You own more than one property, and the short forms have room for only one."
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

        answers.specialSituations.orEmpty().forEach { blockers += situationBlocker(it) }

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

    private fun situationBlocker(situation: SpecialSituation): String = when (situation) {
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

        SpecialSituation.CLUBBED_INCOME ->
            "Income of another person added to yours has to be shown in the clubbing schedule."

        SpecialSituation.REPRESENTATIVE_ASSESSEE ->
            "Filing as someone's representative rules out the short forms."

        SpecialSituation.TDS_UNDER_194N ->
            "Tax deducted on cash withdrawals under section 194N cannot be claimed on the short forms."
    }
}