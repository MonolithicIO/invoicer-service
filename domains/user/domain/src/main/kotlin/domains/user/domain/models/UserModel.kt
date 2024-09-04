package domains.user.domain.models

import java.util.*

data class UserModel(
    val id: UUID,
    val password: String,
    val verified: Boolean,
    val email: String,
)
