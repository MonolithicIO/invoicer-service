package io.github.alaksion.invoicer.foundation.messaging

interface MessageProducer {
    suspend fun produceMessage(
        topic: MessageTopic,
        key: String,
        value: String
    )
}
