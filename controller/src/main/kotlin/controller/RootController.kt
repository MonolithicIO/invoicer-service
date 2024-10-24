package controller

import io.ktor.server.application.Application
import io.ktor.server.routing.routing

internal fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
        beneficiaryController()
        intermediaryController()
    }
}