package io.github.alaksion.invoicer.consumers

import io.github.alaksion.invoicer.consumers.messages.MessageSerializer
import io.github.alaksion.invoicer.consumers.strategy.context.MessageContext
import io.github.alaksion.invoicer.foundation.messaging.MessageConsumer
import kotlinx.serialization.json.Json

interface InvoicerMessageConsumer {
    suspend fun consume()
    fun close()
}

internal class InvoicerMessageConsumerImpl(
    private val messageConsumer: MessageConsumer,
    private val messageContext: MessageContext
) : InvoicerMessageConsumer {

    override suspend fun consume() {
        messageConsumer
            .consumeMessages()
            .collect { message ->
                runCatching {
                    Json.decodeFromString(MessageSerializer, message)
                }.onSuccess { parsedMessage ->
                    messageContext.executeStrategy(parsedMessage)
                }.onFailure {
                    // Log incorrect message serialization
                }
            }
    }

    override fun close() {
        messageConsumer.close()
    }
}