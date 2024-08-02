package io.github.alaksion.invoicer.server.domain.model.user

import java.util.*

data class UserModel(
    val id: UUID,
    val password: String,
    val verified: Boolean,
    val email: String,
)
