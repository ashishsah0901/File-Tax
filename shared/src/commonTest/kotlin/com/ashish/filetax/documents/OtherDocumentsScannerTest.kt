package com.ashish.filetax.documents

import com.ashish.filetax.documents.logic.DocumentScanner
import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.DocumentKind
import com.ashish.filetax.documents.model.FigureKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OtherDocumentsScannerTest {

    @Test
    fun aFullAndFinalSettlementGivesUpLeaveEncashmentAndGratuity() {
        val parsed = DocumentScanner.parse("f&f.pdf", DocumentSamples.fullAndFinalSettlement)

        assertEquals(DocumentKind.FULL_AND_FINAL_SETTLEMENT, parsed.kind)
        assertEquals(268000L, parsed.amountOf(FigureKind.LEAVE_ENCASHMENT))
        assertEquals(480000L, parsed.amountOf(FigureKind.GRATUITY))
        assertEquals(90000L, parsed.amountOf(FigureKind.NOTICE_PAY_RECOVERY))
        assertEquals(62000L, parsed.amountOf(FigureKind.TDS_ON_SALARY))
        assertNull(parsed.amountOf(FigureKind.VRS_COMPENSATION))
    }

    @Test
    fun form12baGivesUpThePerquisites() {
        val parsed = DocumentScanner.parse("12ba.pdf", DocumentSamples.form12ba)

        assertEquals(DocumentKind.FORM_12BA, parsed.kind)
        assertEquals(412000L, parsed.amountOf(FigureKind.ESOP_PERQUISITE))
        assertEquals(474000L, parsed.amountOf(FigureKind.PERQUISITES))
    }

    @Test
    fun form10eGivesUpArrearsAndRelief() {
        val parsed = DocumentScanner.parse("10e.pdf", DocumentSamples.form10e)

        assertEquals(DocumentKind.FORM_10E, parsed.kind)
        assertEquals(340000L, parsed.amountOf(FigureKind.SALARY_ARREARS))
        assertEquals(28500L, parsed.amountOf(FigureKind.RELIEF_SECTION_89))
    }

    @Test
    fun anEsopStatementIsReadAsSalaryPerquisite() {
        val parsed = DocumentScanner.parse("esop.pdf", DocumentSamples.esopStatement)

        assertEquals(DocumentKind.ESOP_PERQUISITE_STATEMENT, parsed.kind)
        assertEquals(412000L, parsed.amountOf(FigureKind.ESOP_PERQUISITE))
        assertEquals(128000L, parsed.amountOf(FigureKind.TDS_ON_SALARY))
    }

    @Test
    fun rentRecordsGiveUpRentAndMunicipalTax() {
        val parsed = DocumentScanner.parse("rent.pdf", DocumentSamples.rentRecords)

        assertEquals(DocumentKind.RENT_RECORDS, parsed.kind)
        assertEquals(480000L, parsed.amountOf(FigureKind.RENT_RECEIVED))
        assertEquals(12000L, parsed.amountOf(FigureKind.MUNICIPAL_TAX_PAID))
        assertEquals(9600L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun aSaleDeedGivesUpEverythingScheduleCgAsksFor() {
        val parsed = DocumentScanner.parse("deed.pdf", DocumentSamples.propertySale)

        assertEquals(DocumentKind.PROPERTY_SALE_PAPERS, parsed.kind)
        assertEquals(9500000L, parsed.amountOf(FigureKind.SALE_OF_IMMOVABLE_PROPERTY))
        assertEquals(9800000L, parsed.amountOf(FigureKind.STAMP_DUTY_VALUE))
        assertEquals(4200000L, parsed.amountOf(FigureKind.COST_OF_ACQUISITION))
        assertEquals(95000L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun form16bIsReadAsTdsOtherThanSalary() {
        val parsed = DocumentScanner.parse("16b.pdf", DocumentSamples.form16b)

        assertEquals(DocumentKind.FORM_16B_OR_16C, parsed.kind)
        assertEquals(95000L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun aProfitAndLossAccountIsReadLineByLine() {
        val parsed = DocumentScanner.parse("pnl.pdf", DocumentSamples.profitAndLoss)

        assertEquals(DocumentKind.PROFIT_AND_LOSS, parsed.kind)
        assertEquals(8450000L, parsed.amountOf(FigureKind.BUSINESS_RECEIPTS))
        assertEquals(3350000L, parsed.amountOf(FigureKind.BUSINESS_GROSS_PROFIT))
        assertEquals(210000L, parsed.amountOf(FigureKind.DEPRECIATION))
        assertEquals(1980000L, parsed.amountOf(FigureKind.BUSINESS_EXPENSES))
        assertEquals(1370000L, parsed.amountOf(FigureKind.BUSINESS_NET_PROFIT))
    }

    @Test
    fun aBalanceSheetIsReadForThePartABsRows() {
        val parsed = DocumentScanner.parse("bs.pdf", DocumentSamples.balanceSheet)

        assertEquals(DocumentKind.BALANCE_SHEET, parsed.kind)
        assertEquals(620000L, parsed.amountOf(FigureKind.SUNDRY_DEBTORS))
        assertEquals(480000L, parsed.amountOf(FigureKind.CLOSING_STOCK))
        assertEquals(350000L, parsed.amountOf(FigureKind.SUNDRY_CREDITORS))
        assertEquals(275000L, parsed.amountOf(FigureKind.CASH_BALANCE))
    }

    @Test
    fun aTurnoverSummaryIsSplitIntoBankedAndCash() {
        val parsed = DocumentScanner.parse("turnover.pdf", DocumentSamples.turnoverSummary)

        assertEquals(DocumentKind.TURNOVER_SUMMARY, parsed.kind)
        assertEquals(4800000L, parsed.amountOf(FigureKind.BUSINESS_RECEIPTS))
        assertEquals(4200000L, parsed.amountOf(FigureKind.DIGITAL_TURNOVER))
        assertEquals(600000L, parsed.amountOf(FigureKind.CASH_TURNOVER))
    }

    @Test
    fun aGstReturnGivesUpTheTurnoverTheDepartmentAlreadyHas() {
        val parsed = DocumentScanner.parse("gstr3b.pdf", DocumentSamples.gstReturn)

        assertEquals(DocumentKind.GST_RETURN, parsed.kind)
        assertEquals(4500000L, parsed.amountOf(FigureKind.GST_TURNOVER))
    }

    @Test
    fun aFirmStatementSeparatesExemptShareFromBusinessIncome() {
        val parsed = DocumentScanner.parse("firm.pdf", DocumentSamples.firmShare)

        assertEquals(DocumentKind.FIRM_SHARE_STATEMENT, parsed.kind)
        assertEquals(850000L, parsed.amountOf(FigureKind.PARTNER_SHARE_OF_PROFIT))
        assertEquals(1200000L, parsed.amountOf(FigureKind.PARTNER_REMUNERATION))
        assertEquals(180000L, parsed.amountOf(FigureKind.PARTNER_INTEREST_ON_CAPITAL))
    }

    @Test
    fun aCryptoStatementIsReadForScheduleVda() {
        val parsed = DocumentScanner.parse("vda.pdf", DocumentSamples.cryptoStatement)

        assertEquals(DocumentKind.CRYPTO_STATEMENT, parsed.kind)
        assertEquals(540000L, parsed.amountOf(FigureKind.VDA_SALE_CONSIDERATION))
        assertEquals(620000L, parsed.amountOf(FigureKind.VDA_COST_OF_ACQUISITION))
        assertEquals(5400L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun aMutualFundStatementIsToldApartFromABrokerStatement() {
        val parsed = DocumentScanner.parse("cams.pdf", DocumentSamples.mutualFundStatement)

        assertEquals(DocumentKind.MUTUAL_FUND_STATEMENT, parsed.kind)
        assertEquals(32000L, parsed.amountOf(FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY))
        assertEquals(96000L, parsed.amountOf(FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY))
        assertEquals(415000L, parsed.amountOf(FigureKind.SALE_OF_SECURITIES))
    }

    @Test
    fun deductionProofsAreSplitBySection() {
        val parsed = DocumentScanner.parse("proofs.pdf", DocumentSamples.deductionProofs)

        assertEquals(DocumentKind.DEDUCTION_PROOFS, parsed.kind)
        assertEquals(48000L, parsed.amountOf(FigureKind.DEDUCTION_80C))
        assertEquals(26000L, parsed.amountOf(FigureKind.DEDUCTION_80D))
        assertEquals(15000L, parsed.amountOf(FigureKind.DEDUCTION_80G))
        assertEquals(62000L, parsed.amountOf(FigureKind.DEDUCTION_80E))
    }

    @Test
    fun lastYearsReturnGivesUpTheLossToCarryForward() {
        val parsed = DocumentScanner.parse("itrv.pdf", DocumentSamples.lastYearReturn)

        assertEquals(DocumentKind.LAST_YEAR_RETURN, parsed.kind)
        assertEquals(1245000L, parsed.amountOf(FigureKind.LAST_YEAR_TOTAL_INCOME))
        assertEquals(185000L, parsed.amountOf(FigureKind.CARRIED_FORWARD_LOSS))
    }

    @Test
    fun agriculturalRecordsAreReadForScheduleEi() {
        val parsed = DocumentScanner.parse("agri.pdf", DocumentSamples.agriculturalRecords)

        assertEquals(DocumentKind.AGRICULTURAL_RECORDS, parsed.kind)
        assertEquals(620000L, parsed.amountOf(FigureKind.AGRICULTURAL_INCOME))
    }

    @Test
    fun foreignTaxProofIsReadForSchedulesFsiAndTr() {
        val parsed = DocumentScanner.parse("form67.pdf", DocumentSamples.foreignTaxProof)

        assertEquals(DocumentKind.FOREIGN_TAX_PROOF, parsed.kind)
        assertEquals(1850000L, parsed.amountOf(FigureKind.FOREIGN_INCOME))
        assertEquals(275000L, parsed.amountOf(FigureKind.FOREIGN_TAX_PAID))
    }

    @Test
    fun aWinningsCertificateIsReadGross() {
        val parsed = DocumentScanner.parse("winnings.pdf", DocumentSamples.winningsCertificate)

        assertEquals(DocumentKind.WINNINGS_CERTIFICATE, parsed.kind)
        assertEquals(250000L, parsed.amountOf(FigureKind.WINNINGS))
        assertEquals(75000L, parsed.amountOf(FigureKind.TDS_OTHER_THAN_SALARY))
    }

    @Test
    fun aSalarySlipIsReadAsACrossCheckOnFormSixteen() {
        val parsed = DocumentScanner.parse("payslip.pdf", DocumentSamples.salarySlip)

        assertEquals(DocumentKind.SALARY_SLIP, parsed.kind)
        assertEquals(158000L, parsed.amountOf(FigureKind.GROSS_SALARY))
        assertEquals(200L, parsed.amountOf(FigureKind.PROFESSIONAL_TAX))
        assertEquals(18267L, parsed.amountOf(FigureKind.TDS_ON_SALARY))
    }

    @Test
    fun form12bbGivesUpTheLandlordsPan() {
        val parsed = DocumentScanner.parse("12bb.pdf", DocumentSamples.form12bb)

        assertEquals(DocumentKind.FORM_12BB, parsed.kind)
        assertEquals("ZZZZZ9999Z", parsed.valueOf(DetailKind.LANDLORD_PAN))
        assertEquals(198000L, parsed.amountOf(FigureKind.HOME_LOAN_INTEREST_PAID))
        assertEquals(150000L, parsed.amountOf(FigureKind.DEDUCTION_80C))
    }

    @Test
    fun form27dIsReadAsTaxCollectedAtSource() {
        val parsed = DocumentScanner.parse("27d.pdf", DocumentSamples.form27d)

        assertEquals(DocumentKind.FORM_27D, parsed.kind)
        assertEquals(25000L, parsed.amountOf(FigureKind.TCS))
        assertEquals("MUMH12345C", parsed.valueOf(DetailKind.COLLECTOR_TAN))
    }

    @Test
    fun aFifteenHDeclarationIsRecognisedEvenThoughNothingIsEntered() {
        val parsed = DocumentScanner.parse("15h.pdf", DocumentSamples.form15h)

        assertEquals(DocumentKind.FORM_15G_OR_15H, parsed.kind)
        assertTrue(parsed.figures.isEmpty())
    }

    @Test
    fun aTradingStatementSeparatesIntradayFromFuturesAndOptions() {
        val parsed = DocumentScanner.parse("taxpnl.pdf", DocumentSamples.tradingProfitAndLoss)

        assertEquals(DocumentKind.TRADING_PROFIT_AND_LOSS, parsed.kind)
        assertEquals(84000L, parsed.amountOf(FigureKind.SPECULATIVE_BUSINESS_INCOME))
        assertEquals(265000L, parsed.amountOf(FigureKind.DERIVATIVES_INCOME))
        assertEquals(48000L, parsed.amountOf(FigureKind.SHORT_TERM_CAPITAL_GAIN_EQUITY))
        assertEquals(172000L, parsed.amountOf(FigureKind.LONG_TERM_CAPITAL_GAIN_EQUITY))
    }
}