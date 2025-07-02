package controller.features

import controller.Constants
import controller.viewmodel.customer.CreateCustomerResponseViewModel
import controller.viewmodel.customer.CreateCustomerViewModel
import controller.viewmodel.customer.toModel
import controller.viewmodel.customer.toViewModel
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlin.getValue
import kotlin.text.toIntOrNull
import kotlin.text.toLongOrNull
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.customer.CreateCustomerService
import services.api.services.customer.ListCustomersService

internal fun Routing.customerController() {
    route("/v1/company/{companyId}") {
        jwtProtected {
            post("/customer") {
                val companyId = call.parameters["companyId"]!!
                val body = call.receive<CreateCustomerViewModel>()
                val service by closestDI().instance<CreateCustomerService>()

                call.respond(
                    status = HttpStatusCode.Created,
                    message = CreateCustomerResponseViewModel(
                        service.createCustomer(
                            userId = parseUuid(jwtUserId()),
                            data = body.toModel(companyId)
                        )
                    )
                )
            }
        }

        jwtProtected {
            get("/customers") {
                val companyId = call.parameters["companyId"]!!
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: Constants.DEFAULT_PAGE
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_LIMIT
                val service by closestDI().instance<ListCustomersService>()

                call.respond(
                    status = HttpStatusCode.Created,
                    message = service.list(
                        userId = parseUuid(jwtUserId()),
                        page = page,
                        limit = limit,
                        companyId = parseUuid(companyId),
                    ).toViewModel()
                )
            }
        }
    }
}
