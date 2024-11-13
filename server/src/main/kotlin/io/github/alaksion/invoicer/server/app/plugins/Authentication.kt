package io.github.alaksion.invoicer.server.app.plugins

import foundation.api.SecretsProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import foundation.authentication.api.jwt.appJwt

fun Application.installAuth(
    secretsProvider: SecretsProvider
) {
    install(Authentication) {
        appJwt(secretsProvider)
    }
}