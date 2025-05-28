package controller

import controller.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import controller.viewmodel.company.CreateCompanyViewModel
import controller.viewmodel.company.toModel
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.company.CreateCompanyService

internal fun Routing.companyController() {
    route("/v1/company") {
        jwtProtected {
            post {
                val body = call.receive<CreateCompanyViewModel>()
                val service by closestDI().instance<CreateCompanyService>()

                call.respond(
                    message =
                        CreateBeneficiaryResponseViewModel(
                            id = service.createCompany(
                                data = body.toModel(),
                                userId = parseUuid(jwtUserId())
                            )
                        ),
                    status = HttpStatusCode.Created
                )
            }
        }
    }
}