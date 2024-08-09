package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.*
import utils.exceptions.HttpError
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal interface GetInvoiceByIdUseCase {
    suspend fun get(id: String, userId: String): InvoiceModel
}

internal class GetInvoiceByIdUseCaseImpl(
    private val repository: InvoiceRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : GetInvoiceByIdUseCase {

    override suspend fun get(id: String, userId: String): InvoiceModel {
        val user = getUserByIdUseCase.get(userId)
        val invoice = repository.getInvoiceByUUID(id = UUID.fromString(id)) ?: throw HttpError(
            statusCode = HttpStatusCode.NotFound,
            message = "Invoice with id ${id} not found"
        )

        if (user.id != invoice.user.id) {
            unauthorizedResourceError()
        }

        return invoice
    }

}