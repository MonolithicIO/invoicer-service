package io.github.monolithic.invoicer.consumers.strategy

import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage

internal interface SendEmailProcessor {
    suspend fun process(message: SendEmailMessage)
}

internal class SendEmailProcessorImpl : SendEmailProcessor {
    override suspend fun process(message: SendEmailMessage) = Unit
}
