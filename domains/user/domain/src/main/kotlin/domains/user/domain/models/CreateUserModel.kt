package domains.user.domain.models

data class CreateUserModel(
    val email: String,
    val confirmEmail: String,
    val password: String
)