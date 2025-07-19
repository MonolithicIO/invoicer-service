package io.github.monolithic.invoicer.controller.features

import io.github.monolithic.invoicer.controller.viewmodel.login.LoginResponseViewModel
import io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken.RequestQrCodeTokenViewModel
import io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken.toDomainModel
import io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken.toTokenDetailsViewModel
import io.github.monolithic.invoicer.controller.viewmodel.qrcodetoken.toTokenResponseViewModel
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError
import io.github.monolithic.invoicer.services.qrcodetoken.AuthorizeQrCodeTokenService
import io.github.monolithic.invoicer.services.qrcodetoken.GetQrCodeTokenByContentIdService
import io.github.monolithic.invoicer.services.qrcodetoken.PollAuthorizedTokenService
import io.github.monolithic.invoicer.services.qrcodetoken.RequestQrCodeTokenService
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.origin
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlin.time.Duration.Companion.seconds
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

internal fun Routing.loginCodeController() {
    route("/v1/login_code") {
        requestQrCodeToken()
        consumeQrCodeToken()
        getQrCodeTokenByContentId()
        scanStatusWebSocket()
    }
}

private fun Route.scanStatusWebSocket() {
    webSocket("/qrcode_socket/{contentId}") {
        val contentId = call.parameters["contentId"] ?: forbiddenError()
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

private fun Route.requestQrCodeToken() = post {
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

private fun Route.consumeQrCodeToken() = jwtProtected {
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

private fun Route.getQrCodeTokenByContentId() = jwtProtected {
    get("/{contentId}") {
        val contentId = call.parameters["contentId"] ?: forbiddenError()
        val service by closestDI().instance<GetQrCodeTokenByContentIdService>()
        val result = service.find(contentId) ?: notFoundError("QrCode not found")
        call.respond(
            status = HttpStatusCode.OK,
            message = result.toTokenDetailsViewModel()
        )
    }
}
