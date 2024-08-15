package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceActivityModel
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetBeneficiaryByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.GetIntermediaryByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.response.CreateInvoiceResponseViewModel
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.LocalDate
import utils.date.api.DateProvider
import utils.exceptions.HttpError
import utils.exceptions.httpError
import java.util.UUID

internal interface CreateInvoiceUseCase {
    suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: String
    ): CreateInvoiceResponseViewModel
}

internal class CreateInvoiceUseCaseImpl(
    private val invoiceRepository: InvoiceRepository,
    private val dateProvider: DateProvider,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase,
    private val getIntermediaryByIdUseCase: GetIntermediaryByIdUseCase
) : CreateInvoiceUseCase {

    override suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: String,
    ): CreateInvoiceResponseViewModel {
        validateActivities(model.activities)

        validateDateRange(
            issueDate = model.issueDate,
            dueDate = model.dueDate
        )

        getBeneficiaryByIdUseCase.get(
            beneficiaryId = model.beneficiaryId,
            userId = userId
        )

        if (model.intermediaryId != null) {
            getIntermediaryByIdUseCase.get(
                intermediaryId = model.intermediaryId,
                userId = userId
            )
        }

        getUserByIdUseCase.get(userId)

        if (invoiceRepository.getInvoiceByExternalId(externalId = model.externalId) != null) {
            throw HttpError(
                message = "Invoice with externalId: ${model.externalId} already exists",
                statusCode = HttpStatusCode.Conflict
            )
        }

        val response =
            invoiceRepository.createInvoice(data = model, userId = UUID.fromString(userId))

        return CreateInvoiceResponseViewModel(
            externalInvoiceId = model.externalId,
            invoiceId = response
        )
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