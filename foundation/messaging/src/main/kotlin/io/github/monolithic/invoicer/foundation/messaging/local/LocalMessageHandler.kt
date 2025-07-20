package io.github.monolithic.invoicer.foundation.messaging.local

import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.messaging.MessageConsumer
import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class LocalMessageHandler(
    private val logger: Logger
) : MessageConsumer, MessageProducer {

    private val messageFlow = MutableSharedFlow<String>()

    override val messageStream: Flow<String> = messageFlow

    override suspend fun startConsuming() {
        logger.log(
            type = LocalMessageHandler::class,
            message = "Message handler is starting to consume messages",
            level = LogLevel.Debug
        )
        // no - op
    }

    override fun close() {
        logger.log(
            type = LocalMessageHandler::class,
            message = "Message handler is closing",
            level = LogLevel.Debug
        )
        // no - op
    }

    override suspend fun produceMessage(
        topic: MessageTopic,
        key: String,
        value: String
    ) {
        logger.log(
            type = LocalMessageHandler::class,
            message = "Producing message to topic: ${topic.name}, key: $key, value: $value",
            level = LogLevel.Debug
        )
        messageFlow.emit(value)
    }
}
