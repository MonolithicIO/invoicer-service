package io.github.monolithic.invoicer.foundation.email.templates

import io.github.monolithic.invoicer.foundation.email.templates.body.forgotPasswordTemplate

object EmailTemplates {
    fun forgotPassword(
        recoveryCode: String,
        expirationText: String
    ) = forgotPasswordTemplate(
        recoveryCode = recoveryCode,
        expirationText = expirationText
    )
}
