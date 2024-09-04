package domains.user.domain.api.models

data class CreateUserModel(
    val email: String,
    val confirmEmail: String,
    val password: String
)