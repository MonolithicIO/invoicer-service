package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import utils.exceptions.unauthorizedResourceError
import java.util.*

interface DeleteInvoiceUseCase {
    suspend fun delete(
        invoiceId: String,
        userId: String
    )
}

internal class DeleteInvoiceUseCaseImpl(
    private val getInvoiceByIdUseCase: GetInvoiceByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val repository: InvoiceRepository
) : DeleteInvoiceUseCase {

    override suspend fun delete(invoiceId: String, userId: String) {
        val user = getUserByIdUseCase.get(userId)
        val invoice = getInvoiceByIdUseCase.get(
            id = invoiceId,
            userId = userId
        )

        if (user.id != invoice.user.id) {
            unauthorizedResourceError()
        }

        repository.deleteByUUID(UUID.fromString(invoiceId))
    }

}