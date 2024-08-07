package io.github.alaksion.invoicer.server.app

import DatabaseFactory
import io.github.alaksion.invoicer.server.app.plugins.configureSerialization
import io.github.alaksion.invoicer.server.app.plugins.installDi
import io.github.alaksion.invoicer.server.app.plugins.installStatusPages
import io.github.alaksion.invoicer.server.view.controller.invoiceController
import io.github.alaksion.invoicer.server.view.controller.userController
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.connect()
    installDi()
    configureSerialization()
    installStatusPages()
    invoiceController()
    userController()
}
