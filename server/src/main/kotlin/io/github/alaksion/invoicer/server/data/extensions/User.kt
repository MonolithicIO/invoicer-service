package io.github.alaksion.invoicer.server.data.extensions

import entities.UserEntity
import io.github.alaksion.invoicer.server.domain.model.user.UserModel


internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)