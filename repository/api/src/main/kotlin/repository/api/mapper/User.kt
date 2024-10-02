package repository.api.mapper

import entities.UserEntity
import models.user.UserModel


internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)