package io.github.alaksion.invoicer.server.app.plugins

import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.appJwt
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