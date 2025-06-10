package controller.features

import controller.viewmodel.login.LoginResponseViewModel
import controller.viewmodel.qrcodetoken.RequestQrCodeTokenViewModel
import controller.viewmodel.qrcodetoken.toDomainModel
import controller.viewmodel.qrcodetoken.toTokenDetailsViewModel
import controller.viewmodel.qrcodetoken.toTokenResponseViewModel
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.qrcodetoken.AuthorizeQrCodeTokenService
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.PollAuthorizedTokenService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.unauthorizedResourceError
import kotlin.time.Duration.Companion.seconds

internal fun Routing.loginCodeController() {
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
                val service by closestDI().instance<AuthorizeQrCodeTokenService>()
                service.consume(
                    contentId = qrCodeContentId,
                    userUuid = parseUuid(jwtUserId())
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
            val findTokenService by closestDI().instance<PollAuthorizedTokenService>()

            val pollResult = findTokenService.poll(
                contentId = contentId,
                interval = 1.seconds
            )

            when (pollResult) {
                is PollAuthorizedTokenService.Response.CloseConnection ->
                    close(
                        reason = CloseReason(
                            code = 1000,
                            message = pollResult.message
                        )
                    )

                is PollAuthorizedTokenService.Response.Success -> {
                    sendSerialized(
                        LoginResponseViewModel(
                            token = pollResult.token.accessToken,
                            refreshToken = pollResult.token.refreshToken
                        )
                    )
                    close(reason = CloseReason(CloseReason.Codes.NORMAL, "Authentication successful"))
                }
            }
        }
    }

}