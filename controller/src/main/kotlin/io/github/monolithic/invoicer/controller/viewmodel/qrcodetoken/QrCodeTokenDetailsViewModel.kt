package io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenModel
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenStatusModel

@Serializable
internal data class QrCodeTokenDetailsViewModel(
    val id: String,
    val agent: String,
    val base64Content: String,
    val rawContent: String,
    val ipAddress: String,
    val status: QrCodeTokenStatusViewModel,
    val createdAt: Instant,
    val updatedAt: Instant,
    val expiresAt: Instant
)

@Serializable
internal enum class QrCodeTokenStatusViewModel {
    GENERATED,
    CONSUMED,
    EXPIRED
}

internal fun QrCodeTokenModel.toTokenDetailsViewModel(): QrCodeTokenDetailsViewModel {
    return QrCodeTokenDetailsViewModel(
        id = id.toString(),
        agent = agent,
        base64Content = base64Content,
        rawContent = rawContent,
        status = when (status) {
            QrCodeTokenStatusModel.GENERATED -> QrCodeTokenStatusViewModel.GENERATED
            QrCodeTokenStatusModel.CONSUMED -> QrCodeTokenStatusViewModel.CONSUMED
            QrCodeTokenStatusModel.EXPIRED -> QrCodeTokenStatusViewModel.EXPIRED
        },
        createdAt = createdAt,
        updatedAt = updatedAt,
        expiresAt = expiresAt,
        ipAddress = ipAddress
    )
}
