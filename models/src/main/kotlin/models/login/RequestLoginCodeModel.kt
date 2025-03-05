package models.login

data class RequestLoginCodeModel(
    val ipAddress: String,
    val agent: String,
    val size: Int,
)
