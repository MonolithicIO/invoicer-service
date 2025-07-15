package io.github.monolithic.invoicer.foundation.messaging

import kotlinx.coroutines.flow.Flow

interface MessageConsumer {
    suspend fun startConsuming()
    fun close()
    val messageStream: Flow<String>
}
