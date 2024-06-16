package invoicer.alaksiondev.com.service

import invoicer.alaksiondev.com.entities.InvoiceEntity
import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.repository.InvoiceRepository
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