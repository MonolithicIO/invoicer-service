package io.github.alaksion.invoicer.server.view.controller

import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
        beneficiaryController()
        intermediaryController()
    }
}