package io.github.alaksion.invoicer.server.app.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import utils.authentication.api.jwt.appJwt

fun Application.installAuth() {
    install(Authentication) {
        appJwt()
    }
}