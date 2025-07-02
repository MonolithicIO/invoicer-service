package controller

import controller.features.authController
import controller.features.companyController
import controller.features.customerController
import controller.features.invoiceController
import controller.features.loginCodeController
import controller.features.payAccountController
import controller.features.userController
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
        loginCodeController()
        companyController()
        payAccountController()
        customerController()
    }
}
