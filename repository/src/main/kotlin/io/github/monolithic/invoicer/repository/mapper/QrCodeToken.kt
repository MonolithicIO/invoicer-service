package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.repository.entities.QrCodeTokenEntity
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenModel
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenStatusModel

internal fun QrCodeTokenEntity.toModel(): QrCodeTokenModel = QrCodeTokenModel(
    id = id.value,
    agent = agent,
    base64Content = base64Content,
    status = when (status) {
        "generated" -> QrCodeTokenStatusModel.GENERATED
        "consumed" -> QrCodeTokenStatusModel.CONSUMED
        "expired" -> QrCodeTokenStatusModel.EXPIRED
        else -> throw IllegalArgumentException("Invalid status")
    },
    createdAt = createdAt,
    updatedAt = updatedAt,
    expiresAt = expiresAt,
    rawContent = content,
    ipAddress = ipAddress
)
