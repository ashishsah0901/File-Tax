package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.CrossCheck
import com.ashish.filetax.documents.model.CrossCheckStatus
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import com.ashish.filetax.documents.model.ParsedDocument
import com.ashish.filetax.onboarding.model.TaxYear

object CrossChecks {

    private data class Agreement(
        val kind: FigureKind,
        val agreeDetail: String,
        val disagreeDetail: String
    )

    private data class Supporting(
        val kind: FigureKind,
        val documents: List<DocumentKind>,
        val detail: String
    )

    private val agreements = listOf(
        Agreement(
            kind = FigureKind.GROSS_SALARY,
            agreeDetail = "Your employer reported the same salary to the department that your own documents show.",
            disagreeDetail = "The department has a different salary on record. Ask your employer, or add the missing part before you file."
        ),
        Agreement(
            kind = FigureKind.TDS_ON_SALARY,
            agreeDetail = "The tax cut from your salary has reached the department, so you can claim all of it.",
            disagreeDetail = "Claim only what Form 26AS shows. Anything short of it has not reached the department yet, and the deductor has to correct the TDS return."
        ),
        Agreement(
            kind = FigureKind.TDS_OTHER_THAN_SALARY,
            agreeDetail = "Every certificate you imported is also in the department's record.",
            disagreeDetail = "A deductor is missing from one of the two. Claim what Form 26AS carries and chase the rest."
        ),
        Agreement(
            kind = FigureKind.SAVINGS_INTEREST,
            agreeDetail = "The savings interest you have is the savings interest the department knows about.",
            disagreeDetail = "AIS collects every bank. If it is higher, an account is missing from your certificate, and that interest is still taxable."
        ),
        Agreement(
            kind = FigureKind.DEPOSIT_INTEREST,
            agreeDetail = "The deposit interest matches what the department was told.",
            disagreeDetail = "Banks report interest accrued for the year. If your certificate is lower, a deposit or a branch is missing from it."
        ),
        Agreement(
            kind = FigureKind.DIVIDEND,
            agreeDetail = "The dividend matches what the companies reported.",
            disagreeDetail = "Dividend is taxable even when no TDS was cut. Go with the higher of the two unless you can show the AIS entry is wrong."
        ),
        Agreement(
            kind = FigureKind.RENT_RECEIVED,
            agreeDetail = "The rent you have matches what your tenant reported.",
            disagreeDetail = "A tenant who deducts TDS reports the rent to the department. If the figures differ, one of the two is for part of the year only."
        ),
        Agreement(
            kind = FigureKind.SALE_OF_SECURITIES,
            agreeDetail = "The sale value in your statement matches what the depository reported.",
            disagreeDetail = "A demat account or a fund house is missing from one of the two. Every sale has to be in Schedule CG, even the loss-making ones."
        ),
        Agreement(
            kind = FigureKind.WINNINGS,
            agreeDetail = "The winnings match what the payer reported.",
            disagreeDetail = "Winnings are taxed gross at 30 percent. The department goes by the payer's figure, so start from the higher one."
        ),
        Agreement(
            kind = FigureKind.ADVANCE_TAX,
            agreeDetail = "The advance tax you paid is sitting against your PAN, so you can claim it.",
            disagreeDetail = "A challan is missing from one of the two. Only what Form 26AS carries can be claimed, so check the assessment year and the PAN on the challan."
        ),
        Agreement(
            kind = FigureKind.SELF_ASSESSMENT_TAX,
            agreeDetail = "The self assessment tax you paid has been matched to your PAN.",
            disagreeDetail = "A payment takes a few days to reach Form 26AS. If it is still missing, check the BSR code, the challan serial number and the assessment year you paid against."
        ),
        Agreement(
            kind = FigureKind.BUSINESS_RECEIPTS,
            agreeDetail = "Your books and the department's record of your receipts agree.",
            disagreeDetail = "A gap between reported receipts and your books is the most common reason a business return is picked up. Reconcile it before filing."
        )
    )

    private val crossKindChecks = listOf(
        Triple(FigureKind.BUSINESS_RECEIPTS, FigureKind.GST_TURNOVER, "Turnover in your books against the GST returns")
    )

    private val supporting = listOf(
        Supporting(
            kind = FigureKind.SALE_OF_SECURITIES,
            documents = listOf(DocumentKind.CAPITAL_GAINS_STATEMENT, DocumentKind.MUTUAL_FUND_STATEMENT),
            detail = "AIS shows a sale value but not your cost, so it cannot tell you the gain. Import the capital gains statement from your broker or the fund's registrar."
        ),
        Supporting(
            kind = FigureKind.SALE_OF_IMMOVABLE_PROPERTY,
            documents = listOf(DocumentKind.PROPERTY_SALE_PAPERS),
            detail = "Schedule CG needs the cost, the improvement, the stamp duty value and the buyer's details, none of which AIS carries. Import the sale deed."
        ),
        Supporting(
            kind = FigureKind.RENT_RECEIVED,
            documents = listOf(DocumentKind.RENT_RECORDS),
            detail = "Schedule HP needs the municipal taxes and the tenant's details as well. Import your rent records."
        ),
        Supporting(
            kind = FigureKind.BUSINESS_RECEIPTS,
            documents = listOf(DocumentKind.PROFIT_AND_LOSS, DocumentKind.TURNOVER_SUMMARY, DocumentKind.GST_RETURN),
            detail = "Receipts alone do not give you business income. Import the profit and loss account, the turnover summary or the GST return."
        ),
        Supporting(
            kind = FigureKind.WINNINGS,
            documents = listOf(DocumentKind.WINNINGS_CERTIFICATE),
            detail = "The gross winning and the tax cut on it are on the payer's certificate. Import it so the 30 percent is worked out on the right figure."
        ),
        Supporting(
            kind = FigureKind.FOREIGN_REMITTANCE,
            documents = listOf(DocumentKind.FOREIGN_ASSET_DETAILS, DocumentKind.FOREIGN_TAX_PROOF),
            detail = "Money sent abroad often ends up in an account or an asset that Schedule FA asks about. Import whatever you hold outside India."
        )
    )

    fun forDocuments(documents: List<ParsedDocument>): List<CrossCheck> = buildList {
        agreements.forEach { agreement ->
            sameFigureCheck(documents, agreement)?.let(::add)
        }
        crossKindChecks.forEach { (left, right, title) ->
            crossKindCheck(documents, left, right, title)?.let(::add)
        }
        supporting.forEach { missing ->
            supportingDocumentCheck(documents, missing)?.let(::add)
        }
        panCheck(documents)?.let(::add)
        yearCheck(documents)?.let(::add)
        virtualDigitalAssetLossCheck(documents)?.let(::add)
    }

    private fun sameFigureCheck(documents: List<ParsedDocument>, agreement: Agreement): CrossCheck? {
        val holders = documents
            .filter { it.amountOf(agreement.kind) != null }
            .sortedBy { DocumentTrust.rankOf(it.kind) }
        if (holders.size < 2) return null
        val left = holders[0]
        val right = holders[1]
        return compare(
            title = "${agreement.kind.label}: ${left.kind.label} against ${right.kind.label}",
            left = left.amountOf(agreement.kind) ?: return null,
            leftLabel = left.kind.label,
            right = right.amountOf(agreement.kind) ?: return null,
            rightLabel = right.kind.label,
            agreeDetail = agreement.agreeDetail,
            disagreeDetail = agreement.disagreeDetail
        )
    }

    private fun crossKindCheck(
        documents: List<ParsedDocument>,
        leftKind: FigureKind,
        rightKind: FigureKind,
        title: String
    ): CrossCheck? {
        val left = documents.firstNotNullOfOrNull { document -> document.amountOf(leftKind)?.let { document to it } }
        val right = documents.firstNotNullOfOrNull { document -> document.amountOf(rightKind)?.let { document to it } }
        if (left == null || right == null) return null
        return compare(
            title = title,
            left = left.second,
            leftLabel = left.first.kind.label,
            right = right.second,
            rightLabel = right.first.kind.label,
            agreeDetail = "What you declared under GST and what your books show are the same.",
            disagreeDetail = "The return asks for both, and the department compares them. Explain the difference in your records before you file."
        )
    }

    private fun supportingDocumentCheck(documents: List<ParsedDocument>, missing: Supporting): CrossCheck? {
        val reported = documents
            .filter { it.kind == DocumentKind.ANNUAL_INFORMATION_STATEMENT || it.kind == DocumentKind.TAXPAYER_INFORMATION_SUMMARY }
            .firstNotNullOfOrNull { it.amountOf(missing.kind) } ?: return null
        if (documents.any { it.kind in missing.documents }) return null
        return CrossCheck(
            title = "${missing.kind.label} of ${IndianAmountFormat.format(reported)} is in AIS",
            status = CrossCheckStatus.INFORMATION,
            detail = missing.detail
        )
    }

    private fun panCheck(documents: List<ParsedDocument>): CrossCheck? {
        val pans = documents.filter { it.kind in DocumentTrust.taxpayerPanDocuments }.mapNotNull { it.identity.pan }.distinct()
        if (pans.size < 2) return null
        return CrossCheck(
            title = "These documents carry different PANs",
            status = CrossCheckStatus.MISMATCH,
            detail = "Found ${pans.joinToString()}. One of them belongs to someone else, and it should not go into your return."
        )
    }

    private fun yearCheck(documents: List<ParsedDocument>): CrossCheck? {
        val wrongYear = documents.filter { document ->
            if (document.kind == DocumentKind.LAST_YEAR_RETURN) return@filter false
            val year = document.identity.assessmentYear ?: return@filter false
            year.filter { it.isDigit() }.take(4) != TaxYear.ASSESSMENT_YEAR.take(4)
        }
        if (wrongYear.isEmpty()) return null
        return CrossCheck(
            title = "Check the year on ${wrongYear.joinToString { it.fileName }}",
            status = CrossCheckStatus.INFORMATION,
            detail = "You are filing for AY ${TaxYear.ASSESSMENT_YEAR}, covering ${TaxYear.FINANCIAL_YEAR_SPAN}. This document reads as a different year."
        )
    }

    private fun virtualDigitalAssetLossCheck(documents: List<ParsedDocument>): CrossCheck? {
        val statement = documents.firstOrNull { it.kind == DocumentKind.CRYPTO_STATEMENT } ?: return null
        val sale = statement.amountOf(FigureKind.VDA_SALE_CONSIDERATION) ?: return null
        val cost = statement.amountOf(FigureKind.VDA_COST_OF_ACQUISITION) ?: return null
        if (cost <= sale) return null
        return CrossCheck(
            title = "Your virtual digital assets sold at a loss",
            status = CrossCheckStatus.INFORMATION,
            detail = "A loss of ${IndianAmountFormat.format(cost - sale)} cannot be set off against anything, not even a gain on another coin, and it cannot be carried forward. Each transfer still has to be listed in Schedule VDA."
        )
    }

    private fun compare(
        title: String,
        left: Long,
        leftLabel: String,
        right: Long,
        rightLabel: String,
        agreeDetail: String,
        disagreeDetail: String
    ): CrossCheck {
        val difference = left - right
        val tolerance = maxOf(100L, maxOf(left, right) / 100)
        val agrees = difference in -tolerance..tolerance
        val amounts = "$leftLabel ${IndianAmountFormat.format(left)}, $rightLabel ${IndianAmountFormat.format(right)}"
        return CrossCheck(
            title = title,
            status = if (agrees) CrossCheckStatus.MATCH else CrossCheckStatus.MISMATCH,
            detail = if (agrees) {
                "$amounts. $agreeDetail"
            } else {
                "$amounts, a gap of ${IndianAmountFormat.format(if (difference < 0) -difference else difference)}. $disagreeDetail"
            }
        )
    }
}