package io.github.monolithic.invoicer.controller

import io.github.monolithic.invoicer.controller.features.authController
import io.github.monolithic.invoicer.controller.features.companyController
import io.github.monolithic.invoicer.controller.features.customerController
import io.github.monolithic.invoicer.controller.features.invoiceController
import io.github.monolithic.invoicer.controller.features.loginCodeController
import io.github.monolithic.invoicer.controller.features.payAccountController
import io.github.monolithic.invoicer.controller.features.userController
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
