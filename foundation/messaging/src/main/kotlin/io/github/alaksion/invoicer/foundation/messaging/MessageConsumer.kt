package io.github.alaksion.invoicer.foundation.messaging

import kotlinx.coroutines.flow.Flow

interface MessageConsumer {
    suspend fun consumeMessages(): Flow<String>
    fun close()
}