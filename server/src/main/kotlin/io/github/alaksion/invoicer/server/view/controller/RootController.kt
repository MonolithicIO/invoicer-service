package io.github.alaksion.invoicer.server.view.controller

import domains.user.controller.userController
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