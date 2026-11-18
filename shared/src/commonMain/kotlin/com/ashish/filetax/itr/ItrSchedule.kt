package com.ashish.filetax.itr

enum class ItrSchedule(val label: String) {
    FILING_DETAILS("Who you are and where the refund goes (Part A)"),
    SALARY("Income from Salary (Schedule S)"),
    HOUSE_PROPERTY("House property (Schedule HP)"),
    OTHER_SOURCES("Other sources (Schedule OS)"),
    CAPITAL_GAINS("Capital gains (Schedule CG)"),
    VIRTUAL_DIGITAL_ASSETS("Virtual digital assets (Schedule VDA)"),
    BUSINESS("Business or profession (Schedules BP, P&L)"),
    BALANCE_SHEET("Balance sheet (Part A-BS)"),
    DEDUCTIONS("Deductions (Schedule VI-A)"),
    TAXES_PAID("Taxes already paid (Schedules TDS, TCS and IT)"),
    FOREIGN("Foreign income and assets (Schedules FSI, TR and FA)"),
    LOSSES("Losses carried forward (Schedule CFL)"),
    EXEMPT("Exempt income (Schedule EI)"),
    COMPUTED("The portal works these out for you"),
    NO_ENTRY("Nothing to enter, keep it for your records")
}