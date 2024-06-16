package invoicer.alaksiondev.com.service

import invoicer.alaksiondev.com.entities.InvoiceEntity
import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.repository.InvoiceRepository
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