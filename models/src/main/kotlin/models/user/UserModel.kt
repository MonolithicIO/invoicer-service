package models.user

import kotlinx.datetime.Instant
import java.util.*

data class UserModel(
    val id: UUID,
    val password: String,
    val verified: Boolean,
    val email: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
