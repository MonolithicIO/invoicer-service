package io.github.alaksion.invoicer.server.app

import DatabaseFactory
import foundation.api.SecretsProvider
import io.github.alaksion.invoicer.server.app.plugins.configureSerialization
import io.github.alaksion.invoicer.server.app.plugins.installAuth
import io.github.alaksion.invoicer.server.app.plugins.installDi
import io.github.alaksion.invoicer.server.app.plugins.installStatusPages
import io.github.alaksion.invoicer.server.view.controller.rootController
import io.ktor.server.application.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    val secrets by closestDI().instance<SecretsProvider>()

    installAuth(
        secretsProvider = secrets
    )
    configureSerialization()
    installStatusPages()
    rootController()
    DatabaseFactory.connect(
        secretsProvider = secrets
    )
}
