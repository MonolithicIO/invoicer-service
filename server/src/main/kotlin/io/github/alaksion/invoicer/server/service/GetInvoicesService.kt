package io.github.alaksion.invoicer.server.service

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.errors.HttpError
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.ktor.http.*

internal interface GetInvoicesService {
    suspend fun get(id: String): InvoiceEntity
}

internal class GetInvoicesServiceImpl(
    private val repository: InvoiceRepository
) : GetInvoicesService {

    override suspend fun get(id: String): InvoiceEntity {
        val result = repository.getInvoiceByExternalId(externalId = id)

        return result ?: throw HttpError(
            statusCode = HttpStatusCode.NotFound,
            message = "Invoice with external id: ${id} not found"
        )
    }
}