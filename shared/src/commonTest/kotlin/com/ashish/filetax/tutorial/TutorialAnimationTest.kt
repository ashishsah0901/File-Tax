package com.ashish.filetax.tutorial

import com.ashish.filetax.onboarding.logic.FilingGuide
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
import kotlin.test.assertTrue

class TutorialAnimationTest {

    private val everything = OnboardingAnswers(
        residentialStatus = ResidentialStatus.RESIDENT,
        incomeSources = IncomeSource.entries.toSet(),
        capitalGainKinds = CapitalGainKind.entries.toSet(),
        housePropertyCount = HousePropertyCount.MORE_THAN_TWO,
        businessKinds = BusinessKind.entries.toSet(),
        presumptiveTurnoverAboveLimit = false,
        agriculturalIncomeAboveLimit = true,
        totalIncomeBand = TotalIncomeBand.ABOVE_ONE_CRORE,
        specialSituations = SpecialSituation.entries.toSet()
    )

    @Test
    fun everyAnimationIsWatchableAndCaptioned() {
        TutorialAnimation.entries.forEach { animation ->
            assertTrue(animation.caption.isNotBlank(), "${animation.name} has no caption")
            assertTrue(
                animation.durationMillis in 2000..8000,
                "${animation.name} runs for ${animation.durationMillis}ms"
            )
        }
    }

    @Test
    fun everyStepOfEveryFormComesWithATutorial() {
        ItrForm.entries.forEach { form ->
            FilingGuide.forForm(form, everything).forEach { step ->
                assertTrue(step.tip.length > 30, "${step.id} has no real tip")
                assertTrue(step.tip.trim().endsWith("."), "${step.id} tip is not a sentence")
            }
        }
    }

    @Test
    fun noAnimationIsDrawnAndThenNeverShown() {
        val used = ItrForm.entries
            .flatMap { form -> FilingGuide.forForm(form, everything) }
            .map { it.animation }
            .toSet() + TutorialAnimation.PRIVACY + TutorialAnimation.SCAN

        assertEquals(TutorialAnimation.entries.toSet(), used)
    }

    @Test
    fun theStepsOfOneFormDoNotAllLookTheSame() {
        val animations = FilingGuide.forForm(ItrForm.ITR1, everything).map { it.animation }

        assertTrue(animations.distinct().size >= animations.size - 1)
    }
}