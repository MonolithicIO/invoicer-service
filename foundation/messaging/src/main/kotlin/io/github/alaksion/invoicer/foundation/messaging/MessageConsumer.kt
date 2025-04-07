package io.github.alaksion.invoicer.foundation.messaging

import kotlinx.coroutines.flow.Flow

interface MessageConsumer {
    suspend fun startConsuming()
    fun close()
    val messageStream: Flow<String>
}