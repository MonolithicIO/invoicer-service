package controller.features

import controller.viewmodel.payaccount.UpdatePayAccountViewModel
import controller.viewmodel.payaccount.toModel
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.company.UpdatePayAccountService

internal fun Routing.payAccountController() {
    route("/v1/company/{companyId}/pay_account") {
        jwtProtected {
            put("/{payAccountId}") {
                val body = call.receive<UpdatePayAccountViewModel>()
                val payAccountId = call.parameters["payAccountId"] ?: ""
                val companyId = call.parameters["companyId"] ?: ""
                val service by closestDI().instance<UpdatePayAccountService>()

                service.update(
                    companyId = parseUuid(companyId),
                    userId = parseUuid(jwtUserId()),
                    model = body.toModel(parseUuid(payAccountId))
                )
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}