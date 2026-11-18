package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BooleanChoice
import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.Choice
import com.ashish.filetax.onboarding.model.HousePropertyCount
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.Question
import com.ashish.filetax.onboarding.model.QuestionId
import com.ashish.filetax.onboarding.model.ResidentialStatus
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TotalIncomeBand
import com.ashish.filetax.onboarding.model.TaxYear

object OnboardingQuestions {

    private val residentialStatus = Question.SingleChoice(
        id = QuestionId.RESIDENTIAL_STATUS,
        title = "Did you live in India last year?",
        subtitle = "Last year means ${TaxYear.FINANCIAL_YEAR_SPAN}. Only the days you were actually in the country count.",
        choices = listOf(
            Choice(
                key = ResidentialStatus.RESIDENT.name,
                label = "Yes, I was here most of the year",
                hint = "182 days or more. Also pick this if you were here 60 days or more last year and 365 days or more over the four years before it."
            ),
            Choice(
                key = ResidentialStatus.NOT_ORDINARILY_RESIDENT.name,
                label = "I moved back to India in the last year or two",
                hint = "You were an NRI before that. The rules put you in a middle category with fewer things to report."
            ),
            Choice(
                key = ResidentialStatus.NON_RESIDENT.name,
                label = "No, I live abroad",
                hint = "Under 60 days in India, or under 182 days if you left for a job abroad or work on an Indian ship."
            )
        )
    )

    private val incomeSources = Question.MultipleChoice(
        id = QuestionId.INCOME_SOURCES,
        title = "How did you earn money last year?",
        subtitle = "Tick everything, even if the amount was small. Bank interest counts.",
        choices = listOf(
            Choice(
                key = IncomeSource.SALARY_PENSION.name,
                label = "A salary or a pension",
                hint = "A job, your own pension, or a family pension. You would have a Form 16 or a pension slip for it."
            ),
            Choice(
                key = IncomeSource.HOUSE_PROPERTY.name,
                label = "A house or flat I own",
                hint = "Rent you collected, or a home loan you are paying on the house you live in."
            ),
            Choice(
                key = IncomeSource.INTEREST_DIVIDEND_OTHER.name,
                label = "Interest or dividends",
                hint = "Interest from your savings account or fixed deposits, dividends on shares, interest on a tax refund."
            ),
            Choice(
                key = IncomeSource.CAPITAL_GAINS.name,
                label = "Something I sold",
                hint = "Shares, mutual funds, a flat, land, gold, bonds."
            ),
            Choice(
                key = IncomeSource.BUSINESS_PROFESSION.name,
                label = "Work I did on my own",
                hint = "A shop, consulting, freelancing, tuition, commission, and also intraday or F&O trading."
            ),
            Choice(
                key = IncomeSource.VIRTUAL_DIGITAL_ASSETS.name,
                label = "Crypto or NFTs"
            ),
            Choice(
                key = IncomeSource.WINNINGS.name,
                label = "Winnings from a lottery, betting or games",
                hint = "Includes online gaming and fantasy sports."
            ),
            Choice(
                key = IncomeSource.AGRICULTURE.name,
                label = "Farming",
                hint = "Selling what you grew, or rent from farmland."
            )
        ),
        noneOfTheseLabel = "None of these"
    )

    private val capitalGainKinds = Question.MultipleChoice(
        id = QuestionId.CAPITAL_GAIN_KINDS,
        title = "What did you sell?",
        subtitle = "What it was, and how long you held it, both change the way the profit is taxed.",
        choices = listOf(
            Choice(
                key = CapitalGainKind.LISTED_EQUITY_LONG_TERM.name,
                label = "Shares or equity mutual funds I held for over a year"
            ),
            Choice(
                key = CapitalGainKind.LISTED_EQUITY_SHORT_TERM.name,
                label = "Shares or equity mutual funds I held for under a year"
            ),
            Choice(
                key = CapitalGainKind.LAND_OR_BUILDING.name,
                label = "A flat, a house or land"
            ),
            Choice(
                key = CapitalGainKind.OTHER_ASSETS.name,
                label = "Something else",
                hint = "Debt funds, bonds, gold, jewellery, shares in a company that is not listed, or anything you own abroad."
            )
        )
    )

    private val equityLongTermGainLimit = Question.SingleChoice(
        id = QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT,
        title = "Was your profit on those under ₹1.25 lakh?",
        subtitle = "Profit is the selling price minus what you paid. Up to ₹1.25 lakh a year it is tax free, and the shortest form still works for you.",
        choices = listOf(
            Choice(key = BooleanChoice.YES, label = "Yes, under ₹1.25 lakh"),
            Choice(key = BooleanChoice.NO, label = "No, more than ₹1.25 lakh")
        )
    )

    private val housePropertyCount = Question.SingleChoice(
        id = QuestionId.HOUSE_PROPERTY_COUNT,
        title = "How many houses or flats do you own?",
        subtitle = "Count the one you live in, the ones you rent out, and anything you own jointly. Two is the new ceiling for the short forms this year.",
        choices = listOf(
            Choice(key = HousePropertyCount.ONE.name, label = "One"),
            Choice(
                key = HousePropertyCount.TWO.name,
                label = "Two",
                hint = "From AY ${TaxYear.ASSESSMENT_YEAR} two houses still fit in ITR-1 and ITR-4. Last year they did not."
            ),
            Choice(key = HousePropertyCount.MORE_THAN_TWO.name, label = "Three or more")
        )
    )

    private val presumptiveTurnoverLimit = Question.SingleChoice(
        id = QuestionId.PRESUMPTIVE_TURNOVER_LIMIT,
        title = "Did you stay inside the presumptive limits?",
        subtitle = "Section 44AD allows sales up to ₹2 crore, or ₹3 crore if no more than 5% of your receipts and payments were in cash. Section 44ADA allows fees up to ₹50 lakh, or ₹75 lakh on the same cash condition. Section 44AE is for up to ten goods vehicles.",
        choices = listOf(
            Choice(key = BooleanChoice.NO, label = "Yes, I stayed inside them"),
            Choice(
                key = BooleanChoice.YES,
                label = "No, I went over",
                hint = "Then the presumptive route is closed for the year: you declare your real profit, and an audit may be due."
            )
        )
    )

    private val businessKinds = Question.MultipleChoice(
        id = QuestionId.BUSINESS_KINDS,
        title = "How do you want to show that income?",
        subtitle = "You can declare a fixed share of what you earned and skip the accounts, or work out your real profit after expenses.",
        choices = listOf(
            Choice(
                key = BusinessKind.PRESUMPTIVE_BUSINESS_44AD.name,
                label = "A fixed share of my sales",
                hint = "The easy route for a shop, trading or a small business, if your sales stayed under ₹3 crore. Known as section 44AD."
            ),
            Choice(
                key = BusinessKind.PRESUMPTIVE_PROFESSION_44ADA.name,
                label = "A fixed share of my fees",
                hint = "The easy route for doctors, lawyers, engineers, architects, accountants and consultants, if your fees stayed under ₹75 lakh. Known as section 44ADA."
            ),
            Choice(
                key = BusinessKind.PRESUMPTIVE_GOODS_CARRIAGE_44AE.name,
                label = "A fixed amount for each goods vehicle I run",
                hint = "For up to ten vehicles. Known as section 44AE."
            ),
            Choice(
                key = BusinessKind.REGULAR_BOOKS.name,
                label = "My real profit, after expenses",
                hint = "Pick this if you keep accounts, or if you earned more than the limits above."
            ),
            Choice(
                key = BusinessKind.PARTNER_IN_FIRM.name,
                label = "I am a partner in a firm",
                hint = "Your share of the firm's profit, or the salary and interest it pays you."
            ),
            Choice(
                key = BusinessKind.SPECULATIVE_OR_DERIVATIVES.name,
                label = "Intraday or F&O trading",
                hint = "Buying and selling the same day, or futures and options."
            )
        )
    )

    private val agriculturalIncomeLimit = Question.SingleChoice(
        id = QuestionId.AGRICULTURAL_INCOME_LIMIT,
        title = "Did you earn more than ₹5,000 from farming?",
        subtitle = "Farm income is not taxed either way. Above ₹5,000 you only have to mention it, and that needs a longer form.",
        choices = listOf(
            Choice(key = BooleanChoice.YES, label = "More than ₹5,000"),
            Choice(key = BooleanChoice.NO, label = "₹5,000 or less")
        )
    )

    private val totalIncomeBand = Question.SingleChoice(
        id = QuestionId.TOTAL_INCOME_BAND,
        title = "How much did you earn in the year, all together?",
        subtitle = "Everything you ticked, added up. ₹50 lakh is where the short forms stop, and ₹1 crore is where the return starts asking for your assets.",
        choices = listOf(
            Choice(key = TotalIncomeBand.UP_TO_FIFTY_LAKH.name, label = "₹50 lakh or less"),
            Choice(key = TotalIncomeBand.FIFTY_LAKH_TO_ONE_CRORE.name, label = "Between ₹50 lakh and ₹1 crore"),
            Choice(
                key = TotalIncomeBand.ABOVE_ONE_CRORE.name,
                label = "More than ₹1 crore",
                hint = "Schedule AL then wants everything you own and owe as on 31 March."
            )
        )
    )

    private val specialSituations = Question.MultipleChoice(
        id = QuestionId.SPECIAL_SITUATIONS,
        title = "Last one. Did any of these happen?",
        subtitle = "Easy to forget, and every one of them changes your form.",
        choices = listOf(
            Choice(
                key = SpecialSituation.COMPANY_DIRECTOR.name,
                label = "I was a director in a company",
                hint = "Your name is on the company's board, even if it paid you nothing."
            ),
            Choice(
                key = SpecialSituation.UNLISTED_SHARES.name,
                label = "I owned shares in a company that is not on the stock market",
                hint = "Startup ESOPs, a private company you invested in, or your own company."
            ),
            Choice(
                key = SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS.name,
                label = "I have money or property outside India",
                hint = "A foreign bank account, shares or RSUs from a foreign employer, a flat abroad, or an account you can sign on."
            ),
            Choice(
                key = SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF.name,
                label = "I earned something abroad, or paid tax abroad"
            ),
            Choice(
                key = SpecialSituation.FOREIGN_RETIREMENT_ACCOUNT.name,
                label = "I have a retirement account abroad",
                hint = "A 401(k), an IRA or a similar account kept in a foreign country. Section 89A lets you defer the tax on it, and from this year that keeps you off the short forms."
            ),
            Choice(
                key = SpecialSituation.CARRIED_FORWARD_LOSS.name,
                label = "I have a loss to carry to next year, or an old loss to use up",
                hint = "A loss on shares, property or your business, from this year or an earlier one."
            ),
            Choice(
                key = SpecialSituation.DEFERRED_ESOP_TAX.name,
                label = "I put off paying tax on startup ESOPs"
            ),
            Choice(
                key = SpecialSituation.CLUBBED_INCOME.name,
                label = "My child's or spouse's income is taxed in my name",
                hint = "For example, interest on a deposit in your minor child's name."
            ),
            Choice(
                key = SpecialSituation.INCOME_APPORTIONED_UNDER_5A.name,
                label = "My spouse and I split our income under the Goa civil code",
                hint = "Section 5A, for couples married under the Portuguese Civil Code. Rare, and it rules out the short forms."
            ),
            Choice(
                key = SpecialSituation.UNEXPLAINED_INCOME_AT_SPECIAL_RATE.name,
                label = "I have income taxed at a flat special rate",
                hint = "Cash credits, unexplained investments or money surrendered in a survey, taxed at 60% under section 115BBE."
            ),
            Choice(
                key = SpecialSituation.REPRESENTATIVE_ASSESSEE.name,
                label = "I am filing for someone else",
                hint = "As a guardian, or for someone who cannot file on their own. From AY ${TaxYear.ASSESSMENT_YEAR} this no longer forces a longer form, it only asks for your name, phone and email."
            ),
            Choice(
                key = SpecialSituation.TDS_UNDER_194N.name,
                label = "My bank cut tax when I withdrew cash",
                hint = "Happens on very large cash withdrawals. Known as section 194N."
            )
        ),
        noneOfTheseLabel = "None of these"
    )

    fun byId(id: QuestionId): Question = when (id) {
        QuestionId.RESIDENTIAL_STATUS -> residentialStatus
        QuestionId.INCOME_SOURCES -> incomeSources
        QuestionId.CAPITAL_GAIN_KINDS -> capitalGainKinds
        QuestionId.EQUITY_LONG_TERM_GAIN_LIMIT -> equityLongTermGainLimit
        QuestionId.HOUSE_PROPERTY_COUNT -> housePropertyCount
        QuestionId.BUSINESS_KINDS -> businessKinds
        QuestionId.PRESUMPTIVE_TURNOVER_LIMIT -> presumptiveTurnoverLimit
        QuestionId.AGRICULTURAL_INCOME_LIMIT -> agriculturalIncomeLimit
        QuestionId.TOTAL_INCOME_BAND -> totalIncomeBand
        QuestionId.SPECIAL_SITUATIONS -> specialSituations
    }
}