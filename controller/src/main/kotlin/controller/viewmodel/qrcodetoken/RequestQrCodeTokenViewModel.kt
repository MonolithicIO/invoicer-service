package controller.viewmodel.qrcodetoken

import kotlinx.serialization.Serializable
import models.login.RequestLoginCodeModel
import utils.exceptions.badRequestError

@Serializable
internal data class RequestQrCodeTokenViewModel(
    val size: Int? = null
)

internal fun RequestQrCodeTokenViewModel.toDomainModel(
    ip: String,
    agent: String
): RequestLoginCodeModel {
    return RequestLoginCodeModel(
        ipAddress = ip,
        agent = agent,
        size = this.size ?: badRequestError("Missing required field: size")
    )
}
