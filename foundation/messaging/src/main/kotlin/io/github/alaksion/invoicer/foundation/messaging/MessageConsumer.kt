package io.github.alaksion.invoicer.foundation.messaging

interface MessageConsumer {
    suspend fun consumeMessages(): Flow<Unit>
}