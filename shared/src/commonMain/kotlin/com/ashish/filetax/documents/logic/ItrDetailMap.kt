package com.ashish.filetax.documents.logic

import com.ashish.filetax.documents.model.DetailKind
import com.ashish.filetax.documents.model.ItrField
import com.ashish.filetax.itr.ItrSchedule

object ItrDetailMap {

    fun forKind(kind: DetailKind): ItrField = when (kind) {
        DetailKind.PAN -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Part A general information: PAN",
            note = "The return is filed against this number, and credit for any tax deducted only comes through if the deductor quoted the same PAN."
        )

        DetailKind.AADHAAR_NUMBER -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Part A general information: Aadhaar number",
            note = "The quickest way to verify the return is an OTP on the phone linked to Aadhaar. PAN and Aadhaar have to be linked, and a return left unverified for 30 days counts as never filed."
        )

        DetailKind.BANK_ACCOUNT_NUMBER -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Bank account details: account number",
            note = "Give every account you hold in India, and tick the one you want the refund in. That account has to be pre-validated on the portal first, and the name on it must match your PAN."
        )

        DetailKind.IFSC -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Bank account details: IFSC",
            note = "Eleven characters from your cheque leaf or passbook. A wrong IFSC is the usual reason a refund fails and has to be reissued."
        )

        DetailKind.BANK_NAME -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Bank account details: name of the bank",
            note = "The portal fills this from the IFSC. Check it rather than typing over it."
        )

        DetailKind.DEDUCTOR_TAN -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule TDS1 or TDS2: TAN of the deductor",
            note = "Each row of tax credit is claimed against the TAN that deducted it. Copy it exactly as Form 26AS shows it."
        )

        DetailKind.COLLECTOR_TAN -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule TCS: TAN of the collector",
            note = "Tax collected on a foreign remittance, a car or a sale of goods is claimed against this TAN, from the Form 27D the collector gives you."
        )

        DetailKind.LANDLORD_PAN -> ItrField(
            schedule = ItrSchedule.SALARY,
            field = "Landlord's PAN, against the HRA exemption",
            note = "Needed once the rent you pay crosses ₹1,00,000 in the year. Without it the employer and the department can both refuse the HRA exemption."
        )

        DetailKind.GSTIN -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "GSTIN in the business schedule",
            note = "Every GSTIN registered against your PAN is asked for, with the turnover reported under it."
        )

        DetailKind.CHALLAN_BSR_CODE -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule IT: BSR code of the bank branch",
            note = "Seven digits from the challan. Without it the payment cannot be matched and the tax shows as unpaid."
        )

        DetailKind.CHALLAN_SERIAL_NUMBER -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule IT: challan serial number",
            note = "Five digits from the challan counterfoil, which is also in Part C of Form 26AS."
        )

        DetailKind.CHALLAN_DATE -> ItrField(
            schedule = ItrSchedule.TAXES_PAID,
            field = "Schedule IT: date of deposit",
            note = "The date on the challan. It decides how much interest under sections 234B and 234C is charged."
        )

        DetailKind.ACKNOWLEDGEMENT_NUMBER -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Part A: acknowledgement number of the form or return it belongs to",
            note = "Form 10-IEA has to be filed before the return and its acknowledgement number and date are asked for in Part A. Last year's acknowledgement is what you carry losses forward from."
        )

        DetailKind.AUDIT_REPORT_UDIN -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Audit information: UDIN, the auditor's details and the date the report was signed",
            note = "The audit report has to be filed by the auditor and accepted by you before the return, and the return will not go through without these."
        )

        DetailKind.FIRM_PAN -> ItrField(
            schedule = ItrSchedule.BUSINESS,
            field = "Schedule IF: the firm's name, PAN and your share in it",
            note = "Being a partner rules out ITR-1, ITR-2 and ITR-4, so this goes into ITR-3."
        )

        DetailKind.TENANT_PAN -> ItrField(
            schedule = ItrSchedule.HOUSE_PROPERTY,
            field = "Tenant's name and PAN against the property",
            note = "Asked for when the rent crosses ₹50,000 a month or when your tenant deducted TDS."
        )

        DetailKind.BUYER_PAN -> ItrField(
            schedule = ItrSchedule.CAPITAL_GAINS,
            field = "Buyer's name, PAN, address and share in the sale",
            note = "Schedule CG will not accept a sale of land or building without the buyer's details."
        )

        DetailKind.DONEE_PAN -> ItrField(
            schedule = ItrSchedule.DEDUCTIONS,
            field = "Section 80G table: the donee's name, PAN, address and the donation reference number",
            note = "The deduction is refused if any of these are missing, or if the institution has not reported your donation."
        )

        DetailKind.DIRECTOR_IDENTIFICATION_NUMBER -> ItrField(
            schedule = ItrSchedule.FILING_DETAILS,
            field = "Directorship table in Part A: company name, PAN, DIN and whether its shares are listed",
            note = "Being a director in any company rules out ITR-1 and ITR-4."
        )
    }
}