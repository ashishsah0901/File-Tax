package com.ashish.filetax.onboarding.logic

import com.ashish.filetax.onboarding.model.BusinessKind
import com.ashish.filetax.onboarding.model.CapitalGainKind
import com.ashish.filetax.onboarding.model.IncomeSource
import com.ashish.filetax.onboarding.model.OnboardingAnswers
import com.ashish.filetax.onboarding.model.SpecialSituation
import com.ashish.filetax.onboarding.model.TaxDocument

object DocumentChecklist {

    fun forAnswers(answers: OnboardingAnswers): List<TaxDocument> = buildList {
        add(DocumentCatalog.pan)
        add(DocumentCatalog.aadhaar)
        add(DocumentCatalog.annualInformationStatement)
        add(DocumentCatalog.form26as)
        add(DocumentCatalog.bankAccount)

        if (answers.has(IncomeSource.SALARY_PENSION)) {
            add(DocumentCatalog.form16)
        }
        if (answers.has(IncomeSource.INTEREST_DIVIDEND_OTHER)) {
            add(DocumentCatalog.interestCertificate)
        }
        if (answers.has(IncomeSource.HOUSE_PROPERTY)) {
            add(DocumentCatalog.homeLoanCertificate)
            add(DocumentCatalog.rentRecords)
        }

        addAll(capitalGainDocuments(answers))

        if (answers.has(IncomeSource.VIRTUAL_DIGITAL_ASSETS)) {
            add(DocumentCatalog.cryptoStatement)
        }

        addAll(businessDocuments(answers))

        if (answers.has(IncomeSource.WINNINGS)) {
            add(DocumentCatalog.winningsCertificate)
        }
        if (answers.agriculturalIncomeAboveLimit == true) {
            add(DocumentCatalog.agriculturalRecords)
        }

        addAll(specialSituationDocuments(answers))

        add(DocumentCatalog.deductionProofs)
    }.distinctBy { it.id }

    private fun capitalGainDocuments(answers: OnboardingAnswers): List<TaxDocument> {
        if (!answers.has(IncomeSource.CAPITAL_GAINS)) return emptyList()
        val kinds = answers.capitalGainKinds.orEmpty()
        return buildList {
            if (kinds.any { it == CapitalGainKind.LISTED_EQUITY_LONG_TERM || it == CapitalGainKind.LISTED_EQUITY_SHORT_TERM }) {
                add(DocumentCatalog.brokerStatement)
                add(DocumentCatalog.mutualFundStatement)
            }
            if (CapitalGainKind.LAND_OR_BUILDING in kinds) {
                add(DocumentCatalog.propertySalePapers)
            }
            if (CapitalGainKind.OTHER_ASSETS in kinds) {
                add(DocumentCatalog.otherAssetProof)
            }
        }
    }

    private fun businessDocuments(answers: OnboardingAnswers): List<TaxDocument> {
        if (!answers.has(IncomeSource.BUSINESS_PROFESSION)) return emptyList()
        val kinds = answers.businessKinds.orEmpty()
        return buildList {
            add(DocumentCatalog.businessBankStatements)
            add(DocumentCatalog.form16a)
            if (kinds.any { it.isPresumptive }) {
                add(DocumentCatalog.turnoverSummary)
            }
            if (BusinessKind.REGULAR_BOOKS in kinds || BusinessKind.SPECULATIVE_OR_DERIVATIVES in kinds) {
                add(DocumentCatalog.booksOfAccounts)
            }
            if (BusinessKind.SPECULATIVE_OR_DERIVATIVES in kinds) {
                add(DocumentCatalog.brokerStatement)
            }
            if (BusinessKind.PARTNER_IN_FIRM in kinds) {
                add(DocumentCatalog.firmShareStatement)
            }
            add(DocumentCatalog.gstReturns)
        }
    }

    private fun specialSituationDocuments(answers: OnboardingAnswers): List<TaxDocument> {
        val situations = answers.specialSituations.orEmpty()
        return buildList {
            if (SpecialSituation.FOREIGN_ASSETS_OR_ACCOUNTS in situations) {
                add(DocumentCatalog.foreignAssetDetails)
            }
            if (SpecialSituation.FOREIGN_INCOME_OR_TAX_RELIEF in situations) {
                add(DocumentCatalog.foreignTaxProof)
            }
            if (SpecialSituation.FOREIGN_RETIREMENT_ACCOUNT in situations) {
                add(DocumentCatalog.foreignAssetDetails)
                add(DocumentCatalog.foreignTaxProof)
            }
            if (SpecialSituation.CARRIED_FORWARD_LOSS in situations) {
                add(DocumentCatalog.lastYearReturn)
            }
            if (SpecialSituation.UNLISTED_SHARES in situations) {
                add(DocumentCatalog.unlistedShareDetails)
            }
            if (SpecialSituation.DEFERRED_ESOP_TAX in situations) {
                add(DocumentCatalog.esopPerquisiteStatement)
            }
            if (SpecialSituation.COMPANY_DIRECTOR in situations) {
                add(DocumentCatalog.directorDetails)
            }
            if (SpecialSituation.CLUBBED_INCOME in situations) {
                add(DocumentCatalog.clubbedIncomeProof)
            }
            if (SpecialSituation.REPRESENTATIVE_ASSESSEE in situations) {
                add(DocumentCatalog.representativeAuthority)
            }
        }
    }
}