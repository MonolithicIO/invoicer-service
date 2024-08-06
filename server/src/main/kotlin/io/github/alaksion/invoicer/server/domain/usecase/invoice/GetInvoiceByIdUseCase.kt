package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.errors.HttpError
import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.ktor.http.*
import java.util.*

internal interface GetInvoiceByIdUseCase {
    suspend fun get(id: String): InvoiceModel
}

internal class GetInvoiceByIdUseCaseImpl(
    private val repository: InvoiceRepository
) : GetInvoiceByIdUseCase {

    override suspend fun get(id: String): InvoiceModel {
        val invoice = repository.getInvoiceByUUID(id = UUID.fromString(id))
        return invoice ?: throw HttpError(
            statusCode = HttpStatusCode.NotFound,
            message = "Invoice with id ${id} not found"
        )
    }

}