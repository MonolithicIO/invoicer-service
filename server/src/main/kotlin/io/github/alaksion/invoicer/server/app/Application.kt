package io.github.alaksion.invoicer.server.app

import DatabaseFactory
import io.github.alaksion.invoicer.server.app.plugins.configureSerialization
import io.github.alaksion.invoicer.server.app.plugins.installAuth
import io.github.alaksion.invoicer.server.app.plugins.installDi
import io.github.alaksion.invoicer.server.app.plugins.installStatusPages
import io.github.alaksion.invoicer.server.view.controller.rootController
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.connect()
    installAuth()
    installDi()
    configureSerialization()
    installStatusPages()
    rootController()
}
