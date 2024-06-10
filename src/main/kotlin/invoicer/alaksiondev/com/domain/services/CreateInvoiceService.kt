package invoicer.alaksiondev.com.domain.services

import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceResponseModel
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceServiceModel
import invoicer.alaksiondev.com.domain.repository.IInvoiceRepository
import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.validation.validateSwiftCode
import io.ktor.http.HttpStatusCode
import java.time.LocalDate

interface ICreateInvoiceService {
    suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel
}

internal class CreateInvoiceService(
    private val repository: IInvoiceRepository
) : ICreateInvoiceService {

    override suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel {
        validateServices(model.services)

        validateSwifts(
            beneficiary = model.beneficiary.beneficiarySwift,
            intermediary = model.intermediary?.intermediarySwift
        )
        validateDateRange(
            issueDate = model.issueDate,
            dueDate = model.dueDate
        )

        if (repository.getInvoiceByExternalId(externalId = model.externalId) != null) {
            throw HttpError(
                message = "Invoice with externalId: ${model.externalId} already exists",
                statusCode = HttpStatusCode.Conflict
            )
        }

        val response = repository.createInvoice(data = model)
        return CreateInvoiceResponseModel(
            externalInvoiceId = model.externalId,
            invoiceId = response
        )
    }

    private fun validateSwifts(
        beneficiary: String,
        intermediary: String?
    ) {
        if (validateSwiftCode(beneficiary).not()) {
            throw HttpError(
                message = "Beneficiary SWIFT code is invalid: $beneficiary",
                statusCode = HttpStatusCode.BadRequest
            )
        }

        intermediary?.let { intermediarySwift ->
            if (validateSwiftCode(intermediarySwift).not()) {
                throw HttpError(
                    message = "Beneficiary SWIFT code is invalid: $intermediarySwift",
                    statusCode = HttpStatusCode.BadRequest
                )
            }
        }
    }

    private fun validateDateRange(
        issueDate: LocalDate,
        dueDate: LocalDate
    ) {
        if (issueDate.isAfter(dueDate)) {
            throw HttpError(
                message = "Issue date cannot be after due date",
                statusCode = HttpStatusCode.BadRequest
            )
        }
    }

    private fun validateServices(
        services: List<CreateInvoiceServiceModel>
    ) {
        if (services.isEmpty()) throw HttpError(
            message = "Invoice must have at least one service",
            statusCode = HttpStatusCode.BadRequest
        )
    }

}