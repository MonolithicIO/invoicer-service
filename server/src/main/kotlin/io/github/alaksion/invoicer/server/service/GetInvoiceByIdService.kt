package io.github.alaksion.invoicer.server.service

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.errors.HttpError
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.ktor.http.*
import java.util.*

internal interface GetInvoiceByIdService {
    suspend fun get(id: String): InvoiceEntity
}

internal class GetInvoiceByIdServiceImpl(
    private val repository: InvoiceRepository
) : GetInvoiceByIdService {

    override suspend fun get(id: String): InvoiceEntity {
        val invoice = repository.getInvoiceById(id = UUID.fromString(id), eagerLoadActivities = true)
        return invoice ?: throw HttpError(
            statusCode = HttpStatusCode.NotFound,
            message = "Invoice with id ${id} not found"
        )
    }

}