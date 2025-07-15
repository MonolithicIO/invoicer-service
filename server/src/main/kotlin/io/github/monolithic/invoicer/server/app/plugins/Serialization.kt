package io.github.monolithic.invoicer.server.app.plugins

import io.github.monolithic.invoicer.server.app.config.jsonConfig
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(
            json = jsonConfig
        )
    }
}
