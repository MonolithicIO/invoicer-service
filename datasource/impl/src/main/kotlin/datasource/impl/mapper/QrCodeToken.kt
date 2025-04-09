package datasource.impl.mapper

import datasource.impl.entities.QrCodeTokenEntity
import models.qrcodetoken.QrCodeTokenModel
import models.qrcodetoken.QrCodeTokenStatusModel

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
