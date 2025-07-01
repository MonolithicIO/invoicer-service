package controller

import controller.features.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
        loginCodeController()
        companyController()
        payAccountController()
    }
}