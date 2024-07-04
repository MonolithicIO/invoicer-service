package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.domain.errors.HttpError
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.CreateInvoiceActivityViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.CreateInvoiceViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.CreateInvoiceResponseViewModel
import io.github.alaksion.invoicer.server.repository.InvoiceActivityRepository
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.validation.validateSwiftCode
import io.ktor.http.*
import kotlinx.datetime.LocalDate

internal interface CreateInvoiceUseCase {
    suspend fun createInvoice(model: CreateInvoiceViewModel): CreateInvoiceResponseViewModel
}

internal class CreateInvoiceUseCaseImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceActivityRepository: InvoiceActivityRepository
) : CreateInvoiceUseCase {

    override suspend fun createInvoice(model: CreateInvoiceViewModel): CreateInvoiceResponseViewModel {
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

        val response = invoiceRepository.createInvoice(model = model)
        invoiceActivityRepository.createInvoiceActivities(
            list = model.activities,
            invoiceId = response
        )

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
        if (issueDate >= dueDate) {
            throw HttpError(
                message = "Issue date cannot be after due date",
                statusCode = HttpStatusCode.BadRequest
            )
        }
    }

    private fun validateActivities(
        services: List<CreateInvoiceActivityViewModel>
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