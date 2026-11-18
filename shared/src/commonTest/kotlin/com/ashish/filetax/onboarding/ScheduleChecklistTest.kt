package com.ashish.filetax.onboarding

import com.ashish.filetax.onboarding.logic.ScheduleChecklist
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertTrue

class ScheduleChecklistTest {

    private val salaried = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = setOf(IncomeSource.SALARY_PENSION),
        totalIncomeBand = TotalIncomeBand.UP_TO_FIFTY_LAKH,
        specialSituations = emptySet()
    )

    @Test
    fun everyoneGetsThePartsNobodyCanSkip() {
        val picks = ScheduleChecklist.forForm(ItrForm.ITR1, salaried)

        assertTrue(picks.any { it.name.contains("Part A, general information") })
        assertTrue(picks.any { it.name.contains("TDS1") })
        assertTrue(picks.any { it.name.contains("Part B-TI") })
        assertTrue(picks.all { it.why.isNotBlank() })
    }

    @Test
    fun sellingSharesAddsTheCapitalGainsSchedulesInPairs() {
        val picks = ScheduleChecklist.forForm(
            ItrForm.ITR2,
            salaried.copy(
                incomeSources = salaried.incomeSources!! + IncomeSource.CAPITAL_GAINS,
                capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM)
            )
        )

        assertTrue(picks.any { it.name == "Schedule CG, capital gains" })
        assertTrue(picks.any { it.name == "Schedule 112A" })
    }

    @Test
    fun keepingBooksAddsTheProfitAndLossAndBalanceSheet() {
        val picks = ScheduleChecklist.forForm(
            ItrForm.ITR3,
            salaried.copy(
                incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
                businessKinds = setOf(BusinessKind.REGULAR_BOOKS, BusinessKind.PARTNER_IN_FIRM)
            )
        )

        assertTrue(picks.any { it.name.contains("Part A-P&L") })
        assertTrue(picks.any { it.name == "Schedule IF" })
        assertTrue(picks.any { it.name.contains("Schedule EI") })
    }

    @Test
    fun aCroreOfIncomeAddsTheAssetSchedule() {
        val picks = ScheduleChecklist.forForm(
            ItrForm.ITR2,
            salaried.copy(totalIncomeBand = TotalIncomeBand.ABOVE_ONE_CRORE)
        )

        assertTrue(picks.any { it.name.contains("Schedule AL") })
    }

    @Test
    fun foreignHoldingsAddTheSchedulesThatCarryAPenalty() {
        val picks = ScheduleChecklist.forForm(
            ItrForm.ITR2,
            salaried.copy(
                specialSituations = setOf(
                    SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS,
                    SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF
                )
            )
        )

        assertTrue(picks.any { it.name.contains("Schedule FA") })
        assertTrue(picks.any { it.name.contains("Schedule FSI") })
    }
}