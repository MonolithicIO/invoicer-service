package controller.viewmodel.login

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshAuthRequest(
    val refreshToken: String? = null
)

@Serializable
internal data class RefreshAuthResponse(
    val refreshToken: String,
    val accessToken: String,
)