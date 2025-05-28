package controller

import controller.features.authController
import controller.features.beneficiaryController
import controller.features.companyController
import controller.features.intermediaryController
import controller.features.invoiceController
import controller.features.loginCodeController
import controller.features.userController
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.rootController() {
    routing {
        invoiceController()
        userController()
        authController()
        beneficiaryController()
        intermediaryController()
        loginCodeController()
        companyController()
    }
}