package domains.user.data.api.extensions

import domains.user.domain.api.models.UserModel
import entities.UserEntity

internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email
)