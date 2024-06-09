package invoicer.alaksiondev.com

import DatabaseFactory
import invoicer.alaksiondev.com.plugins.configureSerialization
import invoicer.alaksiondev.com.routes.invoiceRouting
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.database
    configureSerialization()
    invoiceRouting()
//    configureRouting()
    //    configureMonitoring()
}
