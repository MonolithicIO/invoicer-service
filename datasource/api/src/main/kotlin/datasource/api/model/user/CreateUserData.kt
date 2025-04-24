package datasource.api.model.user

data class CreateUserData(
    val email: String,
    val password: String,
    val identityProviderUuid: String?
)