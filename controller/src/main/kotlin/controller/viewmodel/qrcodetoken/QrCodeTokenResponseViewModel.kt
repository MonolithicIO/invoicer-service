package controller.viewmodel.qrcodetoken

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
internal data class QrCodeTokenResponseViewModel(
    val base64Content: String,
    val expiration: Instant,
    val rawContent: String
)
