package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.ItrForm
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.SchedulePick
import com.ashish.filetax.onboarding.model.SpecialSituation

object ScheduleChecklist {

    fun forForm(form: ItrForm, answers: OnboardingAnswers): List<SchedulePick> = buildList {
        add(
            SchedulePick(
                name = "Part A, general information",
                why = "Your PAN, Aadhaar, address, contact details and the bank account the refund goes to. Always on."
            )
        )

        if (answers.has(IncomeSource.SALARY_PENSION)) {
            add(
                SchedulePick(
                    name = if (form == ItrForm.ITR1) "Income from salary" else "Schedule S, salary",
                    why = "Gross salary, the allowances exempt under section 10, and the deductions under section 16."
                )
            )
        }
        if (answers.has(IncomeSource.HOUSE_PROPERTY)) {
            add(
                SchedulePick(
                    name = if (form == ItrForm.ITR1) "Income from house property" else "Schedule HP, house property",
                    why = "One block per house, self-occupied or let out, with rent, municipal tax and loan interest."
                )
            )
        }
        if (answers.has(IncomeSource.INTEREST_DIVIDEND_OTHER) || answers.has(IncomeSource.WINNINGS)) {
            add(
                SchedulePick(
                    name = if (form == ItrForm.ITR1) "Income from other sources" else "Schedule OS, other sources",
                    why = "Interest, dividends, family pension and anything taxed at a special rate, such as winnings."
                )
            )
        }
        if (answers.has(IncomeSource.CAPITAL_GAINS)) {
            add(
                SchedulePick(
                    name = "Schedule CG, capital gains",
                    why = "Every sale, split by asset and by holding period."
                )
            )
            val kinds = answers.capitalGainKinds.orEmpty()
            if (CapitalGainKind.LISTED_EQUITY_LONG_TERM in kinds) {
                add(
                    SchedulePick(
                        name = "Schedule 112A",
                        why = "The scrip-by-scrip detail behind every long term equity sale. Tick it or Schedule CG will not accept the gain."
                    )
                )
            }
        }
        if (answers.has(IncomeSource.VIRTUAL_DIGITAL_ASSETS)) {
            add(
                SchedulePick(
                    name = "Schedule VDA",
                    why = "One row per crypto or NFT transfer, with dates and cost."
                )
            )
        }
        if (answers.has(IncomeSource.BUSINESS_PROFESSION)) {
            val kinds = answers.businessKinds.orEmpty()
            add(
                SchedulePick(
                    name = "Schedule BP, business or profession",
                    why = "Where business income is worked out, whether from books or at the presumptive rate."
                )
            )
            if (kinds.any { !it.isPresumptive } || answers.presumptiveTurnoverAboveLimit == true) {
                add(
                    SchedulePick(
                        name = "Part A-P&L and Part A-BS",
                        why = "The profit and loss account and the balance sheet, line by line, before Schedule BP will compute."
                    )
                )
            }
            if (BusinessKind.PARTNER_IN_FIRM in kinds) {
                add(
                    SchedulePick(
                        name = "Schedule IF",
                        why = "The firm's name, PAN and your share in it."
                    )
                )
            }
        }
        if (answers.has(IncomeSource.AGRICULTURE) || answers.has(BusinessKind.PARTNER_IN_FIRM)) {
            add(
                SchedulePick(
                    name = "Schedule EI, exempt income",
                    why = "Agricultural income and a share of profit from a firm are exempt, but still have to be reported."
                )
            )
        }
        add(
            SchedulePick(
                name = if (form == ItrForm.ITR1) "Total deductions" else "Schedule VI-A, deductions",
                why = "Every deduction from 80C to 80U. Under the new regime only a few survive, so tick it and leave the rest blank."
            )
        )
        if (answers.has(SpecialSituation.CARRIED_FORWARD_LOSS)) {
            add(
                SchedulePick(
                    name = "Schedule CFL, and Schedule BFLA and CYLA with it",
                    why = "Losses brought forward, set off this year, and carried on to the next."
                )
            )
        }
        if (answers.has(SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS) || answers.has(SpecialSituation.FOREIGN_RETIREMENT_ACCOUNT)) {
            add(
                SchedulePick(
                    name = "Schedule FA, foreign assets",
                    why = "Everything held outside India at any time in the calendar year."
                )
            )
        }
        if (answers.has(SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF)) {
            add(
                SchedulePick(
                    name = "Schedule FSI and Schedule TR",
                    why = "Income earned abroad, country by country, and the credit for tax already paid on it."
                )
            )
        }
        if (answers.has(SpecialSituation.CLUBBED_INCOME)) {
            add(
                SchedulePick(
                    name = "Schedule SPI",
                    why = "Income of a spouse or minor child that the law adds to yours."
                )
            )
        }
        if (answers.aboveOneCrore()) {
            add(
                SchedulePick(
                    name = "Schedule AL, assets and liabilities",
                    why = "Compulsory once total income crosses ₹1 crore: what you own and what you owe on 31 March."
                )
            )
        }
        add(
            SchedulePick(
                name = "Schedules TDS1, TDS2, TCS and IT",
                why = "Tax already deducted, collected or paid by you. These come pre-filled from Form 26AS."
            )
        )
        add(
            SchedulePick(
                name = "Part B-TI and Part B-TTI",
                why = "Total income and the tax on it. The portal computes both, and you only check them."
            )
        )
    }
}