package io.github.alaksion.invoicer.server.domain.model.user

data class CreateUserModel(
    val email: String,
    val confirmEmail: String,
    val password: String
)