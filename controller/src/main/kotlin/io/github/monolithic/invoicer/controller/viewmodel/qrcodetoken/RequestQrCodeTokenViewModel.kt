package io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken

import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.login.RequestLoginCodeModel
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

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
