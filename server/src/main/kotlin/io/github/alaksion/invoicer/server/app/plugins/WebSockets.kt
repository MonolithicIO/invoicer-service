package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.config.jsonConfig
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal fun Application.installWebSocket() {
    install(WebSockets) {
        pingPeriod = 30.seconds.toJavaDuration()
        timeout = 60.seconds.toJavaDuration()
        maxFrameSize = Long.MAX_VALUE
        masking = false

        contentConverter = KotlinxWebsocketSerializationConverter(jsonConfig)
    }
}