package services.api.model.user

data class CreateUserModel(
    val email: String,
    val confirmEmail: String,
    val password: String
)