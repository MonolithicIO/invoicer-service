package io.github.monolithic.invoicer.models.user

import io.github.monolithic.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserModel(
    @Serializable(with = JavaUUIDSerializer::class) val id: UUID,
    val password: String,
    val verified: Boolean,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val identityProviderUuid: String?
)
