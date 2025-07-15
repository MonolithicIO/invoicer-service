package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.repository.entities.UserEntity
import io.github.monolithic.invoicer.models.user.UserModel

internal fun UserEntity.toModel(): UserModel = UserModel(
    id = this.id.value,
    password = this.password,
    verified = this.verified,
    email = this.email,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    identityProviderUuid = this.identityProviderUuid,
)
