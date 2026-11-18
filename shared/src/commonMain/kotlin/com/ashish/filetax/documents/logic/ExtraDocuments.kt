package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.onboarding.model.TaxDocument

data class ExtraDocument(val kind: DocumentKind, val whereToGet: String)

object ExtraDocuments {

    val all = listOf(
        ExtraDocument(
            kind = DocumentKind.FULL_AND_FINAL_SETTLEMENT,
            whereToGet = "Your employer's HR if you left a job or retired this year. It carries leave encashment, gratuity, notice pay and anything paid on the way out."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_12BA,
            whereToGet = "Comes attached to Form 16 when you had perquisites, and itemises each one, ESOPs included."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_10E,
            whereToGet = "You fill it yourself on incometax.gov.in under e-File, Income Tax Forms, if you received arrears. It has to be filed before the return."
        ),
        ExtraDocument(
            kind = DocumentKind.SALARY_SLIP,
            whereToGet = "Your employer's payroll portal. March's slip carries the year to date, which is the quickest check on Form 16."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_27D,
            whereToGet = "Whoever collected tax from you: the bank on a foreign remittance, or the dealer on a car. Without it, the TCS is hard to claim back."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_12BB,
            whereToGet = "The declaration you gave your employer at the start of the year, with your rent, landlord's PAN and planned investments."
        ),
        ExtraDocument(
            kind = DocumentKind.TRADING_PROFIT_AND_LOSS,
            whereToGet = "Your broker's console, as the tax P&L for FY 2025-26. It separates intraday, futures and options, and delivery."
        ),
        ExtraDocument(
            kind = DocumentKind.TAX_PAYMENT_CHALLAN,
            whereToGet = "The counterfoil you get after paying advance or self assessment tax, or a reprint from your bank's net banking under tax payments."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_10IEA,
            whereToGet = "You file it on incometax.gov.in under e-File, Income Tax Forms, if you have business income and want to stay on the old regime."
        ),
        ExtraDocument(
            kind = DocumentKind.TAX_AUDIT_REPORT,
            whereToGet = "Your auditor files Form 3CA or 3CB with 3CD on the portal, and you accept it there before filing the return."
        ),
        ExtraDocument(
            kind = DocumentKind.FORM_16B_OR_16C,
            whereToGet = "The buyer of your property, or your tenant, downloads it from TRACES and hands it to you."
        )
    )

    private val scannableChecklistIds = DocumentKind.entries.mapNotNull { it.checklistId }.toSet()

    fun scannable(checklist: List<TaxDocument>): List<TaxDocument> =
        checklist.filter { it.id in scannableChecklistIds }
}