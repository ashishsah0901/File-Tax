package com.ashish.filetax.onboarding.model

import kotlinx.serialization.Serializable

enum class ResidentialStatus {
    RESIDENT,
    NOT_ORDINARILY_RESIDENT,
    NON_RESIDENT
}

enum class IncomeSource {
    SALARY_PENSION,
    HOUSE_PROPERTY,
    INTEREST_DIVIDEND_OTHER,
    CAPITAL_GAINS,
    BUSINESS_PROFESSION,
    VIRTUAL_DIGITAL_ASSETS,
    WINNINGS,
    AGRICULTURE
}

enum class CapitalGainKind {
    LISTED_EQUITY_LONG_TERM,
    LISTED_EQUITY_SHORT_TERM,
    LAND_OR_BUILDING,
    OTHER_ASSETS
}

enum class HousePropertyCount {
    ONE,
    MORE_THAN_ONE
}

enum class BusinessKind(val isPresumptive: Boolean) {
    PRESUMPTIVE_BUSINESS_44AD(true),
    PRESUMPTIVE_PROFESSION_44ADA(true),
    PRESUMPTIVE_GOODS_CARRIAGE_44AE(true),
    REGULAR_BOOKS(false),
    PARTNER_IN_FIRM(false),
    SPECULATIVE_OR_DERIVATIVES(false)
}

enum class SpecialSituation {
    COMPANY_DIRECTOR,
    UNLISTED_SHARES,
    FOREIGN_ASSETS_OR_ACCOUNTS,
    FOREIGN_INCOME_OR_TAX_RELIEF,
    CARRIED_FORWARD_LOSS,
    DEFERRED_ESOP_TAX,
    CLUBBED_INCOME,
    REPRESENTATIVE_ASSESSEE,
    TDS_UNDER_194N
}

@Serializable
data class OnboardingAnswers(
    val assessmentYear: String = TaxYear.ASSESSMENT_YEAR,
    val residentialStatus: ResidentialStatus? = null,
    val incomeSources: Set<IncomeSource>? = null,
    val capitalGainKinds: Set<CapitalGainKind>? = null,
    val equityLongTermGainWithinExemptLimit: Boolean? = null,
    val housePropertyCount: HousePropertyCount? = null,
    val businessKinds: Set<BusinessKind>? = null,
    val agriculturalIncomeAboveLimit: Boolean? = null,
    val totalIncomeAboveFiftyLakh: Boolean? = null,
    val specialSituations: Set<SpecialSituation>? = null
) {
    fun has(source: IncomeSource): Boolean = incomeSources?.contains(source) == true

    fun has(situation: SpecialSituation): Boolean = specialSituations?.contains(situation) == true

    fun has(kind: BusinessKind): Boolean = businessKinds?.contains(kind) == true
}