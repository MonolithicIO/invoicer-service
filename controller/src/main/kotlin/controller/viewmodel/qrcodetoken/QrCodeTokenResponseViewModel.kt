package controller.viewmodel.qrcodetoken

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.qrcodetoken.QrCodeTokenModel

@Serializable
internal data class QrCodeTokenResponseViewModel(
    val base64Content: String,
    val expiration: Instant,
    val rawContent: String
)

internal fun QrCodeTokenModel.toViewModel(): QrCodeTokenResponseViewModel {
    return QrCodeTokenResponseViewModel(
        base64Content = this.base64Content,
        expiration = this.expiresAt,
        rawContent = this.rawContent
    )
}
