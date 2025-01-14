package io.github.alaksion.invoicer.server.app

import connectDatabase
import controller.rootController
import io.github.alaksion.invoicer.server.app.plugins.configureSerialization
import io.github.alaksion.invoicer.server.app.plugins.installAuth
import io.github.alaksion.invoicer.server.app.plugins.installDi
import io.github.alaksion.invoicer.server.app.plugins.installStatusPages
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    installAuth()
    connectDatabase()
    configureSerialization()
    installStatusPages()
    rootController()
}
