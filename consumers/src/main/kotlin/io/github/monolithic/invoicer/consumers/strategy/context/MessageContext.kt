package io.github.monolithic.invoicer.consumers.strategy.context

import io.github.monolithic.invoicer.consumers.messages.types.GeneratePdfMessage
import io.github.monolithic.invoicer.consumers.messages.types.Message
import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.consumers.messages.types.Unkown
import io.github.monolithic.invoicer.consumers.strategy.GeneratePdfStrategy
import io.github.monolithic.invoicer.consumers.strategy.SendEmailStrategy
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger

internal interface MessageContext {
    suspend fun executeStrategy(message: Message)
}

internal class MessageContextImpl(
    private val generatePdfStrategy: GeneratePdfStrategy,
    private val sendEmailStrategy: SendEmailStrategy,
    private val logger: Logger
) : MessageContext {
    override suspend fun executeStrategy(message: Message) {
        runCatching {
            logger.log(
                type = MessageContextImpl::class,
                message = "Type message received: $message",
                level = LogLevel.Debug
            )

            when (message) {
                is GeneratePdfMessage -> generatePdfStrategy.process(message)
                SendEmailMessage -> sendEmailStrategy.process()
                Unkown -> {
                    logger.log(
                        type = MessageContextImpl::class,
                        message = "Unknown message type: not proceeding with processing",
                        level = LogLevel.Warn,
                    )
                }
            }
        }.onFailure { error ->
            logger.log(
                type = MessageContextImpl::class,
                message = "Failed to process message: $message",
                level = LogLevel.Error,
                throwable = error
            )
        }
    }
}
