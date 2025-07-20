package io.github.monolithic.invoicer.server.app

import io.github.monolithic.invoicer.controller.rootController
import io.github.monolithic.invoicer.foundation.authentication.provider.IdentityProvider
import io.github.monolithic.invoicer.processor.ProcessConsumer
import io.github.monolithic.invoicer.server.app.database.connectDatabase
import io.github.monolithic.invoicer.server.app.plugins.configureMonitoring
import io.github.monolithic.invoicer.server.app.plugins.configureSecurity
import io.github.monolithic.invoicer.server.app.plugins.configureSerialization
import io.github.monolithic.invoicer.server.app.plugins.installAuth
import io.github.monolithic.invoicer.server.app.plugins.installDi
import io.github.monolithic.invoicer.server.app.plugins.installStatusPages
import io.github.monolithic.invoicer.server.app.plugins.installWebSocket
import io.ktor.server.application.Application
import kotlinx.datetime.Clock
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    installAuth()
    connectDatabase()
    configureSecurity()
    configureSerialization()

    val clock by closestDI().instance<Clock>()
    installStatusPages(clock = clock)
    configureMonitoring()
    installWebSocket()
    rootController()

    val identity by closestDI().instance<IdentityProvider>()
    identity.initialize()

    val processConsumer by closestDI().instance<ProcessConsumer>()
    processConsumer.startObserving()
}
