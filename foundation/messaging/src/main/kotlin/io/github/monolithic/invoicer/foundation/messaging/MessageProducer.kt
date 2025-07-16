package io.github.monolithic.invoicer.foundation.messaging

interface MessageProducer {
    suspend fun produceMessage(
        topic: MessageTopic,
        key: String,
        value: String
    )
}
