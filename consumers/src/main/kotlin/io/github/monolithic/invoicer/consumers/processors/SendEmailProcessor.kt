package io.github.monolithic.invoicer.consumers.processors

import io.github.monolithic.invoicer.consumers.messages.types.PasswordResetCompletedMessage
import io.github.monolithic.invoicer.consumers.messages.types.ResetPasswordMessage
import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.services.user.SendResetPasswordRequestEmailService
import io.github.monolithic.invoicer.services.user.SendResetPasswordSuccessEmailService

internal interface SendEmailProcessor {
    suspend fun process(message: SendEmailMessage)
}

internal class SendEmailProcessorImpl(
    private val resetPasswordEmailService: SendResetPasswordRequestEmailService,
    private val sendPasswordResetEmailService: SendResetPasswordSuccessEmailService
) : SendEmailProcessor {
    override suspend fun process(message: SendEmailMessage) {

        when (message) {
            is ResetPasswordMessage -> resetPasswordEmailService.send(resetRequestId = message.token)

            is PasswordResetCompletedMessage -> sendPasswordResetEmailService.send(
                email = message.email,
                updateDate = message.updateDate
            )
        }
    }
}
