package models.qrcodetoken

import kotlinx.datetime.Instant

enum class QrCodeTokenStatusModel {
    GENERATED,
    CONSUMED,
    EXPIRED
}

data class QrCodeTokenModel(
    val id: String,
    val agent: String,
    val base64Content: String,
    val rawContent: String,
    val status: QrCodeTokenStatusModel,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expiresAt: Instant
)
