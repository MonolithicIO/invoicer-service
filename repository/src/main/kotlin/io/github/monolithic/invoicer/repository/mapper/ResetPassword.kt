package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordRequestModel
import io.github.monolithic.invoicer.repository.entities.ResetPasswordEntity

internal fun ResetPasswordEntity.toModel(): ResetPasswordRequestModel {
    return ResetPasswordRequestModel(
        safeCode = this.safeCode,
        userId = this.user.id.value,
        expiresAt = this.expiresAt,
        isConsumed = this.isConsumed,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        expirationText = this.expirationText
    )
}