package com.ashish.filetax.onboarding

import com.ashish.filetax.onboarding.logic.ItrFormDeterminer
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ItrFormDeterminerTest {

    private val salariedResident = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = setOf(IncomeSource.SALARY_PENSION, IncomeSource.INTEREST_DIVIDEND_OTHER),
        totalIncomeBand = TotalIncomeBand.UP_TO_FIFTY_LAKH,
        specialSituations = emptySet()
    )

    @Test
    fun salariedResidentWithOneHouseGetsItr1() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.HOUSE_PROPERTY,
            housePropertyCount = HousePropertyCount.ONE
        )

        assertEquals(ItrForm.ITR1, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun exemptEquityLongTermGainStaysOnItr1() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.CAPITAL_GAINS,
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM),
            equityLongTermGainWithinExemptLimit = true
        )

        assertEquals(ItrForm.ITR1, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun equityLongTermGainAboveExemptLimitMovesToItr2() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.CAPITAL_GAINS,
            capitalGainKinds = setOf(CapitalGainKind.LISTED_EQUITY_LONG_TERM),
            equityLongTermGainWithinExemptLimit = false
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun sellingAFlatMovesToItr2() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.CAPITAL_GAINS,
            capitalGainKinds = setOf(CapitalGainKind.LAND_OR_BUILDING)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun twoPropertiesStayOnItr1() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.HOUSE_PROPERTY,
            housePropertyCount = HousePropertyCount.TWO
        )

        assertEquals(ItrForm.ITR1, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun threePropertiesMoveToItr2() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.HOUSE_PROPERTY,
            housePropertyCount = HousePropertyCount.MORE_THAN_TWO
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun incomeAboveFiftyLakhMovesToItr2() {
        val answers = salariedResident.copy(totalIncomeBand = TotalIncomeBand.FIFTY_LAKH_TO_ONE_CRORE)

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun nonResidentMovesToItr2() {
        val answers = salariedResident.copy(residentialStatus = ResidentialStatus.NON_RESIDENT)

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun cryptoMovesToItr2() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.VIRTUAL_DIGITAL_ASSETS
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun foreignAssetsMoveToItr2() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun directorOfACompanyMovesToItr2() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.COMPANY_DIRECTOR)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun presumptiveProfessionalGetsItr4() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_PROFESSION_44ADA)
        )

        val recommendation = ItrFormDeterminer.determine(answers)

        assertEquals(ItrForm.ITR4, recommendation.form)
        assertNotNull(recommendation.alternative)
    }

    @Test
    fun presumptiveIncomeAboveFiftyLakhMovesToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_BUSINESS_44AD),
            totalIncomeBand = TotalIncomeBand.FIFTY_LAKH_TO_ONE_CRORE
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun presumptiveWithPropertySaleMovesToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION, IncomeSource.CAPITAL_GAINS),
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_BUSINESS_44AD),
            capitalGainKinds = setOf(CapitalGainKind.LAND_OR_BUILDING)
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun booksOfAccountsMoveToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.REGULAR_BOOKS)
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun derivativesTradingMovesToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.SALARY_PENSION, IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.SPECULATIVE_OR_DERIVATIVES)
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun partnerInAFirmMovesToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.PARTNER_IN_FIRM)
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun agriculturalIncomeAboveLimitMovesToItr2() {
        val answers = salariedResident.copy(
            incomeSources = salariedResident.incomeSources!! + IncomeSource.AGRICULTURE,
            agriculturalIncomeAboveLimit = true
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun everyRecommendationExplainsItself() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.CARRIED_FORWARD_LOSS)
        )

        assertTrue(ItrFormDeterminer.determine(answers).reasons.isNotEmpty())
    }

    @Test
    fun aForeignRetirementAccountMovesToItr2() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.FOREIGN_RETIREMENT_ACCOUNT)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun incomeSplitUnderTheGoaCivilCodeMovesToItr2() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.INCOME_APPORTIONED_UNDER_5A)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun incomeTaxedAtTheSpecialRateMovesToItr2() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.UNEXPLAINED_INCOME_AT_SPECIAL_RATE)
        )

        assertEquals(ItrForm.ITR2, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun filingForSomeoneElseNoLongerRulesOutItr1() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.REPRESENTATIVE_ASSESSEE)
        )

        val recommendation = ItrFormDeterminer.determine(answers)

        assertEquals(ItrForm.ITR1, recommendation.form)
        assertTrue(recommendation.cautions.any { it.contains("representative") })
    }

    @Test
    fun clubbedIncomeStaysOnItr1ButIsFlagged() {
        val answers = salariedResident.copy(
            specialSituations = setOf(SpecialSituation.CLUBBED_INCOME)
        )

        val recommendation = ItrFormDeterminer.determine(answers)

        assertEquals(ItrForm.ITR1, recommendation.form)
        assertTrue(recommendation.cautions.any { it.contains("clubbed") })
    }

    @Test
    fun turnoverOverThePresumptiveCeilingMovesToItr3() {
        val answers = salariedResident.copy(
            incomeSources = setOf(IncomeSource.BUSINESS_PROFESSION),
            businessKinds = setOf(BusinessKind.PRESUMPTIVE_BUSINESS_44AD),
            presumptiveTurnoverAboveLimit = true
        )

        assertEquals(ItrForm.ITR3, ItrFormDeterminer.determine(answers).form)
    }

    @Test
    fun aboveOneCroreTheScheduleOfAssetsIsCalledOut() {
        val answers = salariedResident.copy(totalIncomeBand = TotalIncomeBand.ABOVE_ONE_CRORE)

        val recommendation = ItrFormDeterminer.determine(answers)

        assertEquals(ItrForm.ITR2, recommendation.form)
        assertTrue(recommendation.cautions.any { it.contains("Schedule AL") })
    }
}