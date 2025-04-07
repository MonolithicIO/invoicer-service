package io.github.alaksion.invoicer.consumers.strategy.context

import io.github.alaksion.invoicer.consumers.messages.types.GeneratePdfMessage
import io.github.alaksion.invoicer.consumers.messages.types.Message
import io.github.alaksion.invoicer.consumers.messages.types.SendEmailMessage
import io.github.alaksion.invoicer.consumers.messages.types.Unkown
import io.github.alaksion.invoicer.consumers.strategy.GeneratePdfStrategy
import io.github.alaksion.invoicer.consumers.strategy.SendEmailStrategy

internal interface MessageContext {
    suspend fun executeStrategy(message: Message)
}

internal class MessageContextImpl(
    private val generatePdfStrategy: GeneratePdfStrategy,
    private val sendEmailStrategy: SendEmailStrategy
) : MessageContext {
    override suspend fun executeStrategy(message: Message) {
        runCatching {
            when (message) {
                is GeneratePdfMessage -> generatePdfStrategy.process(message)
                SendEmailMessage -> sendEmailStrategy.process()
                Unkown -> {
                    // Log unknown message type
                }
            }
        }.onFailure {
            // Log failure on message processing
        }
    }
}