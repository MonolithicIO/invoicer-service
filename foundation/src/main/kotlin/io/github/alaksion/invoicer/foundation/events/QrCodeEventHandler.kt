package io.github.alaksion.invoicer.foundation.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class QrCodeLoginEvent(
    val contentId: String,
    val refreshToken: String,
    val accessToken: String
)

interface QrCodeEventHandler {
    val events: SharedFlow<QrCodeLoginEvent>

    suspend fun publishEvent(event: QrCodeLoginEvent)
}

internal class QrCodeEventHandlerImpl : QrCodeEventHandler {

    private val _events = MutableSharedFlow<QrCodeLoginEvent>()

    override val events: SharedFlow<QrCodeLoginEvent> = _events

    override suspend fun publishEvent(event: QrCodeLoginEvent) {
        _events.emit(event)
    }
}