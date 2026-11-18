package com.ashish.filetax.tutorial

enum class TutorialAnimation(val durationMillis: Int, val caption: String) {
    ELIGIBILITY(4200, "Reading down the list of who has to file"),
    LOGIN(3600, "Your PAN goes in as the user ID"),
    PICK_FORM(4000, "Picking the form for the assessment year"),
    REGIME(4400, "Weighing the new regime against the old"),
    SALARY(4000, "Form 16 figures landing in the salary schedule"),
    HOUSE(4200, "Rent in, loan interest out"),
    CAPITAL_GAINS(4200, "Each sale sorted by asset and holding period"),
    CRYPTO(3600, "Every transfer at the flat 30 percent"),
    OTHER_INCOME(3800, "Interest and dividends collected into one head"),
    EXEMPT_INCOME(3800, "Reported, but not taxed"),
    LOSSES(4400, "Carrying a loss forward year by year"),
    FOREIGN(4600, "Everything held outside India, declared"),
    EXTRA_SCHEDULES(4200, "The schedules that only some people fill"),
    LEDGER(4400, "Your books becoming Schedule BP"),
    PRESUMPTIVE(4000, "A fixed share of turnover, declared as profit"),
    TRADING(3800, "Intraday apart from futures and options"),
    PARTNER(4000, "The firm's share split from what it paid you"),
    DEDUCTIONS(4000, "Each proof taking the taxable figure down"),
    MATCH_TAXES(4400, "Your credits matched against Form 26AS"),
    PAY_TAX(3800, "The balance paid before you submit"),
    SUBMIT(3600, "The return handed over"),
    VERIFY(3400, "Verified within 30 days, or it never counted"),
    CALENDAR(4600, "The dates that decide what this costs"),
    PRIVACY(4200, "It goes in, and nothing comes back out"),
    SCAN(3600, "Read on this phone, figure by figure")
}