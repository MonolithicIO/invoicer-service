package io.github.alaksion.invoicer.consumers

import io.github.alaksion.invoicer.consumers.messages.MessageSerializer
import io.github.alaksion.invoicer.consumers.strategy.context.MessageContext
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import io.github.alaksion.invoicer.foundation.messaging.MessageConsumer
import kotlinx.serialization.json.Json

interface InvoicerMessageConsumer {
    suspend fun consume()
    fun close()
}

internal class InvoicerMessageConsumerImpl(
    private val messageConsumer: MessageConsumer,
    private val messageContext: MessageContext,
    private val logger: Logger
) : InvoicerMessageConsumer {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun consume() {
        messageConsumer.startConsuming()

        messageConsumer
            .messageStream
            .collect { message ->
                runCatching {
                    json.decodeFromString(MessageSerializer, message)
                }.onSuccess { parsedMessage ->
                    messageContext.executeStrategy(parsedMessage)
                }.onFailure {
                    logger.log(
                        type = InvoicerMessageConsumerImpl::class,
                        message = "Failed to deserialize kafka message: $message. Check Json Schema",
                        level = LogLevel.Error,
                        throwable = it
                    )
                }
            }
    }

    override fun close() {
        messageConsumer.close()
    }
}