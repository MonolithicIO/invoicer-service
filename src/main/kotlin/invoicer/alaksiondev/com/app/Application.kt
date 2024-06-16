package invoicer.alaksiondev.com.app

import DatabaseFactory
import invoicer.alaksiondev.com.app.plugins.configureSerialization
import invoicer.alaksiondev.com.app.plugins.installDi
import invoicer.alaksiondev.com.app.plugins.installStatusPages
import invoicer.alaksiondev.com.controllers.invoiceController
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
}
