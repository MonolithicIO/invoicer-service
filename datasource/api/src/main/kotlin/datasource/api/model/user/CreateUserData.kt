package datasource.api.model.user

data class CreateUserData(
    val email: String,
    val confirmEmail: String,
    val password: String
)