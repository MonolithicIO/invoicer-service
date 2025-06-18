package repository.mapper

import models.user.UserModel
import repository.entities.UserEntity

internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    identityProviderUuid = this.identityProviderUuid,
)