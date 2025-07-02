package controller.viewmodel.login

import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshAuthRequestViewModel(
    val refreshToken: String? = null
)
