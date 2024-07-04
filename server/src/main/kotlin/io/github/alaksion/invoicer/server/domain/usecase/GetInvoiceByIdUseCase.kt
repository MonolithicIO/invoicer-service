package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.domain.errors.HttpError
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.ktor.http.*
import java.util.*

internal interface GetInvoiceByIdUseCase {
    suspend fun get(id: String): InvoiceEntity
}

internal class GetInvoiceByIdUseCaseImpl(
    private val repository: InvoiceRepository
) : GetInvoiceByIdUseCase {

    override suspend fun get(id: String): InvoiceEntity {
        val invoice = repository.getInvoiceById(id = UUID.fromString(id), eagerLoadActivities = true)
        return invoice ?: throw HttpError(
            statusCode = HttpStatusCode.NotFound,
            message = "Invoice with id ${id} not found"
        )
    }

}