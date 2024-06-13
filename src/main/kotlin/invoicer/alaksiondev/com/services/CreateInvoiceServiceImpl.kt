package invoicer.alaksiondev.com.services

import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceResponseModel
import invoicer.alaksiondev.com.repository.InvoiceActivityRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import invoicer.alaksiondev.com.validation.validateSwiftCode
import io.ktor.http.HttpStatusCode
import java.time.LocalDate

interface CreateInvoiceService {
    suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel
}

internal class CreateInvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceActivityRepository: InvoiceActivityRepository
) : CreateInvoiceService {

    override suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel {
        validateActivities(model.activities)

        validateSwifts(
            beneficiary = model.beneficiary.beneficiarySwift,
            intermediary = model.intermediary?.intermediarySwift
        )
        validateDateRange(
            issueDate = model.issueDate,
            dueDate = model.dueDate
        )

        if (invoiceRepository.getInvoiceByExternalId(externalId = model.externalId) != null) {
            throw HttpError(
                message = "Invoice with externalId: ${model.externalId} already exists",
                statusCode = HttpStatusCode.Conflict
            )
        }

        val response = invoiceRepository.createInvoice(data = model)
        invoiceActivityRepository.createInvoiceActivities(
            list = model.activities,
            invoiceId = response
        )

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

    private fun validateActivities(
        services: List<CreateInvoiceActivityModel>
    ) {
        if (services.isEmpty()) throw HttpError(
            message = "Invoice must have at least one service",
            statusCode = HttpStatusCode.BadRequest
        )

        services.forEach {
            if (it.quantity <= 0) {
                throw HttpError(
                    message = "Invoice activity must have quantity > 0",
                    statusCode = HttpStatusCode.BadRequest
                )
            }

            if (it.unitPrice <= 0) {
                throw HttpError(
                    message = "Invoice activity must have unitPrice > 0",
                    statusCode = HttpStatusCode.BadRequest
                )
            }
        }
    }

}