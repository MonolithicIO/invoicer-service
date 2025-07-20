package io.github.monolithic.invoicer.foundation.messaging.local

import io.github.monolithic.invoicer.foundation.messaging.MessageConsumer
import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class LocalMessageHandler : MessageConsumer, MessageProducer {

    private val messageFlow = MutableSharedFlow<String>()

    override val messageStream: Flow<String> = messageFlow

    override suspend fun startConsuming() {
        // no - op
    }

    override fun close() {
        // no - op
    }

    override suspend fun produceMessage(
        topic: MessageTopic,
        key: String,
        value: String
    ) {
        messageFlow.emit(value)
    }
}