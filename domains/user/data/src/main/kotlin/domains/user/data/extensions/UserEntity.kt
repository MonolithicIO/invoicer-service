package domains.user.data.extensions

import domains.user.domain.models.UserModel
import entities.UserEntity

internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email
)