package datasource.impl.mapper

import datasource.impl.entities.UserEntity
import models.user.UserModel


internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    identityProviderUuid = this.identityProviderUuid,
)