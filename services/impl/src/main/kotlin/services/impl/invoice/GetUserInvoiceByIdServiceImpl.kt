package services.impl.invoice

import models.InvoiceModelLegacy
import repository.InvoiceRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import utils.exceptions.http.forbiddenError
import java.util.*

internal class GetUserInvoiceByIdServiceImpl(
    private val repository: InvoiceRepository,
    private val getUserByIdUseCase: GetUserByIdService
) : GetUserInvoiceByIdService {

    override suspend fun get(invoiceId: UUID, userId: UUID): InvoiceModelLegacy {
        val user = getUserByIdUseCase.get(userId)
        val invoice = repository.getInvoiceByUUID(id = invoiceId) ?: throw HttpError(
            statusCode = HttpCode.NotFound,
            message = "Invoice with id $invoiceId not found"
        )

        if (user.id != invoice.user.id) {
            forbiddenError()
        }

        return invoice
    }
}