package io.github.monolithic.invoicer.consumers.processors

import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.services.user.SendRestPasswordEmailService

internal interface SendEmailProcessor {
    suspend fun process(message: SendEmailMessage)
}

internal class SendEmailProcessorImpl(
    private val resetPasswordEmailService: SendRestPasswordEmailService
) : SendEmailProcessor {
    override suspend fun process(message: SendEmailMessage) {

        when (message) {
            is SendEmailMessage.ResetPasswordMessage -> resetPasswordEmailService.send(message.token)
        }
    }
}
