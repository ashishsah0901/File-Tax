package com.ashish.filetax.documents.model

enum class DetailKind(val label: String) {
    PAN("PAN"),
    AADHAAR_NUMBER("Aadhaar number"),
    BANK_ACCOUNT_NUMBER("Bank account number"),
    IFSC("IFSC code"),
    BANK_NAME("Bank name"),
    DEDUCTOR_TAN("Deductor's TAN"),
    COLLECTOR_TAN("Collector's TAN"),
    LANDLORD_PAN("Landlord's PAN"),
    GSTIN("GSTIN"),
    CHALLAN_BSR_CODE("Challan BSR code"),
    CHALLAN_SERIAL_NUMBER("Challan serial number"),
    CHALLAN_DATE("Date the tax was paid"),
    ACKNOWLEDGEMENT_NUMBER("Acknowledgement number"),
    AUDIT_REPORT_UDIN("Audit report UDIN"),
    FIRM_PAN("Firm's PAN"),
    TENANT_PAN("Tenant's PAN"),
    BUYER_PAN("Buyer's PAN"),
    DONEE_PAN("Donee's PAN"),
    DIRECTOR_IDENTIFICATION_NUMBER("Director identification number")
}

data class ExtractedDetail(
    val kind: DetailKind,
    val value: String,
    val sourceLine: String
)