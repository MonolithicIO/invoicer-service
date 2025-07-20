package io.github.monolithic.invoicer.consumers

import io.github.monolithic.invoicer.consumers.messages.MessageSerializer
import io.github.monolithic.invoicer.consumers.strategy.context.MessageContext
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.messaging.MessageConsumer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

interface MessageHandler {
    fun consume()
    fun close()
}

internal class MessageHandlerImpl(
    private val messageConsumer: MessageConsumer,
    private val messageContext: MessageContext,
    private val logger: Logger,
    dispatcher: CoroutineDispatcher
) : MessageHandler {

    private val consumerScope = CoroutineScope(dispatcher)
    private val json = Json { ignoreUnknownKeys = true }

    override fun consume() {
        consumerScope.launch {
            messageConsumer.startConsuming()

            messageConsumer
                .messageStream
                .collect { message ->
                    runCatching {
                        logger.log(
                            type = MessageHandlerImpl::class,
                            message = "Raw Received message: $message",
                            level = LogLevel.Debug
                        )
                        json.decodeFromString(MessageSerializer, message)
                    }.onSuccess { parsedMessage ->
                        messageContext.executeStrategy(parsedMessage)
                    }.onFailure {
                        logger.log(
                            type = MessageHandlerImpl::class,
                            message = "Failed to deserialize  message: $message. Check Json Schema",
                            level = LogLevel.Error,
                            throwable = it
                        )
                    }
                }
        }
    }

    override fun close() {
        consumerScope.cancel()
        messageConsumer.close()
    }
}
