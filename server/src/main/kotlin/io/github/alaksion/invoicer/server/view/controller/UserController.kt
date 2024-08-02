package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.view.viewmodel.createuser.request.CreateUserRequestViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createuser.request.receiveUserRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.userController() {
    routing {
        post {
            val body = call.receive<CreateUserRequestViewModel>()
            val parsed = receiveUserRequest(body)
        }

        delete("/{id}") {

        }
    }
}