package controller

import controller.viewmodel.login.LoginResponseViewModel
import controller.viewmodel.qrcodetoken.RequestQrCodeTokenViewModel
import controller.viewmodel.qrcodetoken.toDomainModel
import controller.viewmodel.qrcodetoken.toTokenDetailsViewModel
import controller.viewmodel.qrcodetoken.toTokenResponseViewModel
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.events.QrCodeEventHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.qrcodetoken.ConsumeQrCodeTokenService
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.unauthorizedResourceError
import java.util.concurrent.ConcurrentHashMap

internal fun Routing.loginCodeController() {
    val qrCodeEventChannel by closestDI().instance<QrCodeEventHandler>()
    val sessionMap = ConcurrentHashMap<String, WebSocketServerSession>()

    route("/v1/login_code") {
        post {
            val body = call.receive<RequestQrCodeTokenViewModel>()
            val model = body.toDomainModel(
                ip = call.request.origin.remoteHost,
                agent = call.request.header("User-Agent") ?: "Unknown Agent"
            )
            val requestCodeService by closestDI().instance<RequestQrCodeTokenService>()

            call.respond(
                message = requestCodeService.requestQrCodeToken(
                    request = model
                ).toTokenResponseViewModel(),
                status = HttpStatusCode.Created
            )
        }

        jwtProtected {
            post("/{id}/consume") {
                val qrCodeContentId = call.parameters["id"]!!
                val service by closestDI().instance<ConsumeQrCodeTokenService>()
                service.consume(
                    contentId = qrCodeContentId,
                    userUuid = jwtUserId()
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }

        jwtProtected {
            get("/{contentId}") {
                val contentId = call.parameters["contentId"] ?: unauthorizedResourceError()
                val service by closestDI().instance<GetQrCodeTokenByContentIdService>()
                val result = service.find(contentId) ?: notFoundError("QrCode not found")
                call.respond(
                    status = HttpStatusCode.OK,
                    message = result.toTokenDetailsViewModel()
                )
            }
        }

        webSocket("/qrcode_socket/{contentId}") {
            val contentId = call.parameters["contentId"] ?: unauthorizedResourceError()
            val findTokenService by closestDI().instance<GetQrCodeTokenByContentIdService>()

            val token = findTokenService.find(contentId)
            if (token == null) {
                close(
                    reason = CloseReason(
                        code = 1000,
                        message = "QrCode not found, closing connection"
                    )
                )
            }

            val codeExpiration = token!!.expiresAt - Clock.System.now()
            sessionMap[contentId] = this

            val scanJob = launch {
                qrCodeEventChannel
                    .events
                    .filter { it.contentId == contentId }
                    .collect { event ->
                        val matchingConnection = sessionMap[contentId]

                        if (matchingConnection != null && matchingConnection.isActive) {
                            matchingConnection.sendSerialized(
                                LoginResponseViewModel(
                                    token = event.accessToken,
                                    refreshToken = event.refreshToken
                                )
                            )
                            sessionMap.remove(contentId)
                            matchingConnection.close(
                                reason = CloseReason(CloseReason.Codes.NORMAL, "Authentication successful")
                            )
                        }
                    }
            }

            val expirationJob = launch {
                delay(codeExpiration)
                scanJob.cancel()
                close(
                    reason = CloseReason(
                        code = 1001,
                        message = "QrCode expired, closing connection"
                    )
                )
            }

            scanJob.invokeOnCompletion { expirationJob.cancel() }

            runCatching {
                incoming.consumeEach {
                    // no op
                }
            }.onFailure {
                scanJob.cancel()
            }
        }
    }

}