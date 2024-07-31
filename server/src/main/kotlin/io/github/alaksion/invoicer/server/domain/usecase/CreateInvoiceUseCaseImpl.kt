package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.domain.errors.HttpError
import io.github.alaksion.invoicer.server.domain.errors.httpError
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceActivityModel
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.util.DateProvider
import io.github.alaksion.invoicer.server.validation.validateSwiftCode
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.response.CreateInvoiceResponseViewModel
import io.ktor.http.*
import kotlinx.datetime.LocalDate

internal interface CreateInvoiceUseCase {
    suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseViewModel
}

internal class CreateInvoiceUseCaseImpl(
    private val invoiceRepository: InvoiceRepository,
    private val dateProvider: DateProvider
) : CreateInvoiceUseCase {

    override suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseViewModel {
        validateActivities(model.activities)

        validateSwifts(
            beneficiary = model.beneficiarySwift,
            intermediary = model.intermediarySwift
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

        return CreateInvoiceResponseViewModel(
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
        if (dateProvider.now() > issueDate) {
            httpError(
                message = "Issue date cannot be past date",
                code = HttpStatusCode.BadRequest
            )
        }

        if (dateProvider.now() > dueDate) {
            httpError(
                message = "Due date cannot be past date",
                code = HttpStatusCode.BadRequest
            )
        }

        if (issueDate > dueDate) {
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