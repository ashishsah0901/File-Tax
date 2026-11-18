package com.ashish.filetax.onboarding.ui

import com.ashish.filetax.onboarding.model.QuestionId
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_abacus
import filetax.shared.generated.resources.img_bar_chart
import filetax.shared.generated.resources.img_briefcase
import filetax.shared.generated.resources.img_chart_increasing
import filetax.shared.generated.resources.img_coin
import filetax.shared.generated.resources.img_houses
import filetax.shared.generated.resources.img_ledger
import filetax.shared.generated.resources.img_magnifying_glass_tilted_left
import filetax.shared.generated.resources.img_tractor
import filetax.shared.generated.resources.img_world_map
import org.jetbrains.compose.resources.DrawableResource

internal val QuestionId.image: DrawableResource
    get() = when (this) {
        QuestionId.RESIDENTIAL_STATUS -> Res.drawable.img_world_map
        QuestionId.INCOME_SOURCES -> Res.drawable.img_briefcase
        QuestionId.CAPITAL_GAIN_KINDS -> Res.drawable.img_chart_increasing
        QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT -> Res.drawable.img_coin
        QuestionId.HOUSE_PROPERTY_COUNT -> Res.drawable.img_houses
        QuestionId.BUSINESS_KINDS -> Res.drawable.img_ledger
        QuestionId.PRESUMPTIVE_TURNOVER_LIMIT -> Res.drawable.img_abacus
        QuestionId.AGRICULTURAL_INCOME_LIMIT -> Res.drawable.img_tractor
        QuestionId.TOTAL_INCOME_BAND -> Res.drawable.img_bar_chart
        QuestionId.SPECIAL_SITUATIONS -> Res.drawable.img_magnifying_glass_tilted_left
    }