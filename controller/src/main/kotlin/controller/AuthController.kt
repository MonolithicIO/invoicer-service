package controller

import controller.viewmodel.login.LoginViewModel
import controller.viewmodel.login.RefreshAuthRequest
import controller.viewmodel.login.toDomainModel
import controller.viewmodel.login.toViewModel
import controller.viewmodel.qrcodetoken.RequestQrCodeTokenViewModel
import controller.viewmodel.qrcodetoken.toDomainModel
import controller.viewmodel.qrcodetoken.toViewModel
import io.github.alaksion.invoicer.foundation.events.QrCodeEventHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import utils.exceptions.unauthorizedResourceError

internal fun Routing.authController() {
    val qrCodeEventChannel by closestDI().instance<QrCodeEventHandler>()

    route("/v1/auth") {
        post("/login") {
            val body = call.receive<LoginViewModel>()
            val model = body.toDomainModel()
            val loginService by closestDI().instance<LoginService>()

            call.respond(
                message = loginService.login(model).toViewModel(),
                status = HttpStatusCode.OK
            )
        }

        post("/refresh") {
            val body = call.receive<RefreshAuthRequest>()
            val refreshService by closestDI().instance<RefreshLoginService>()

            call.respond(
                message = refreshService.refreshLogin(
                    refreshToken = body.refreshToken ?: unauthorizedResourceError(),
                ).toViewModel(),
            )
        }

        post("/login/code") {
            val body = call.receive<RequestQrCodeTokenViewModel>()
            val model = body.toDomainModel(
                ip = call.request.origin.remoteHost,
                agent = call.request.header("User-Agent") ?: "Unknown Agent"
            )
            val requestCodeService by closestDI().instance<RequestQrCodeTokenService>()

            call.respond(
                message = requestCodeService.requestQrCodeToken(
                    request = model
                ).toViewModel(),
                status = HttpStatusCode.Created
            )
        }

        webSocket("/login/qrcode_session") {
            val eventChannel = MutableSharedFlow<String>()

            val job = launch {
                eventChannel.collect {
                    send(it)
                }
            }
            runCatching {
                incoming.consumeEach { frame -> }
            }.onFailure { error ->
                print("Websocket failure:${error.message.toString()}")
                job.cancel()
            }
        }
    }
}