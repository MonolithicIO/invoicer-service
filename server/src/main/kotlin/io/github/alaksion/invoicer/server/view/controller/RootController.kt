package io.github.alaksion.invoicer.server.view.controller

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
    }
}