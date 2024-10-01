package services.api.model.user

import kotlinx.datetime.LocalDate
import java.util.*

data class UserModel(
    val id: UUID,
    val password: String,
    val verified: Boolean,
    val email: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
