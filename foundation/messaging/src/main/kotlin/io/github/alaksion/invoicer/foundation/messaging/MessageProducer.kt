package io.github.alaksion.invoicer.foundation.messaging

interface MessageProducer {
    suspend fun produceMessage(
        topic: String,
        key: String,
        value: String
    )
}