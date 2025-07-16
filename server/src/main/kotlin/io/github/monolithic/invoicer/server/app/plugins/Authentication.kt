package io.github.monolithic.invoicer.server.app.plugins

import io.github.monolithic.invoicer.foundation.authentication.token.jwt.appJwt
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.installAuth() {
    val secrets by closestDI().instance<SecretsProvider>()

    install(Authentication) {
        appJwt(secrets)
    }
}
