package io.github.monolithic.invoicer.consumers.processors

import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.services.user.SendResetPasswordRequestEmail
import io.github.monolithic.invoicer.services.user.SendResetPasswordSuccessEmail

internal interface SendEmailProcessor {
    suspend fun process(message: SendEmailMessage)
}

internal class SendEmailProcessorImpl(
    private val resetPasswordEmailService: SendResetPasswordRequestEmail,
    private val sendPasswordResetEmailService: SendResetPasswordSuccessEmail
) : SendEmailProcessor {
    override suspend fun process(message: SendEmailMessage) {

        when (message) {
            is SendEmailMessage.ResetPasswordMessage -> resetPasswordEmailService.send(resetRequestId = message.token)

            is SendEmailMessage.PasswordResetCompletedMessage -> sendPasswordResetEmailService.send(
                email = message.email,
                updateDate = message.updateDate
            )
        }
    }
}
