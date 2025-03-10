package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.config.jsonConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = jsonConfig
        )
    }
}
