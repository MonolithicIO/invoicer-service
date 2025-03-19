package services.impl.invoice

import io.github.alaksion.invoicer.utils.http.HttpCode
import models.InvoiceModel
import repository.api.repository.InvoiceRepository
import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpError
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal class GetInvoiceByIdServiceImpl(
    private val repository: InvoiceRepository,
    private val getUserByIdUseCase: GetUserByIdService
) : GetInvoiceByIdService {

    override suspend fun get(id: String, userId: String): InvoiceModel {
        val user = getUserByIdUseCase.get(userId)
        val invoice = repository.getInvoiceByUUID(id = UUID.fromString(id)) ?: throw HttpError(
            statusCode = HttpCode.NotFound,
            message = "Invoice with id ${id} not found"
        )

        if (user.id != invoice.user.id) {
            unauthorizedResourceError()
        }

        return invoice
    }

}