package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.email.templates.EmailTemplates
import io.github.monolithic.invoicer.utils.date.toFormattedDateTime
import kotlinx.datetime.Instant

interface SendResetPasswordSuccessEmailService {
    suspend fun send(email: String, updateDate: Instant)
}

internal class SendResetPasswordSuccessEmailServiceImpl(
    private val emailSender: EmailSender
) : SendResetPasswordSuccessEmailService {

    override suspend fun send(email: String, updateDate: Instant) {
        emailSender.sendTemplateEmail(
            template = EmailTemplates.resetPasswordSuccess(updateDate.toFormattedDateTime()),
            to = email,
            subject = "Invoicer Password Reset Successful",
        )
    }
}
