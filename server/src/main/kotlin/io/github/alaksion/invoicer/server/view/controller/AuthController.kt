package io.github.alaksion.invoicer.server.view.controller

import io.ktor.server.routing.*

fun Routing.authController() {
    route("auth") {
        post("/login") {

        }
    }
}