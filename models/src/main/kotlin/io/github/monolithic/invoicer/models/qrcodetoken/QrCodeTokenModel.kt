package io.github.monolithic.invoicer.models.qrcodetoken

import kotlinx.datetime.Instant
import java.util.UUID

enum class QrCodeTokenStatusModel {
    GENERATED,
    CONSUMED,
    EXPIRED
}

data class QrCodeTokenModel(
    val id: UUID,
    val agent: String,
    val base64Content: String,
    val ipAddress: String,
    val rawContent: String,
    val status: QrCodeTokenStatusModel,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expiresAt: Instant
)
