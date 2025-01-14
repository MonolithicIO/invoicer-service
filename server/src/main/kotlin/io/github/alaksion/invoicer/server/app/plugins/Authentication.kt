package io.github.alaksion.invoicer.server.app.plugins

import foundation.api.SecretsProvider
import foundation.authentication.api.jwt.appJwt
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.installAuth() {
    val secrets by closestDI().instance<SecretsProvider>()

    install(Authentication) {
        appJwt(secrets)
    }
}