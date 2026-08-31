package com.ashish.filetax.onboarding.model

enum class DocumentArt {
    IDENTITY_CARD,
    SHEET,
    PORTAL,
    PASSBOOK,
    CHART,
    HOUSE,
    COIN,
    GLOBE,
    RECEIPT,
    FOLDER
}

data class TaxDocument(
    val id: String,
    val name: String,
    val why: String,
    val whereToGet: String,
    val art: DocumentArt
)