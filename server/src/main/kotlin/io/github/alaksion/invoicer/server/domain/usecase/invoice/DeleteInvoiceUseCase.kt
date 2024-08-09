package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import utils.exceptions.unauthorizedError
import java.util.*

interface DeleteInvoiceUseCase {
    suspend fun delete(
        invoiceId: String,
        userId: String
    )
}

internal class DeleteInvoiceUseCaseImpl(
    private val getInvoiceByIdUseCase: GetInvoiceByIdUseCase,
    private val getUserByIdUseCase: GetInvoiceByIdUseCase,
    private val repository: InvoiceRepository
) : DeleteInvoiceUseCase {

    override suspend fun delete(invoiceId: String, userId: String) {
        val user = getUserByIdUseCase.get(userId)
        val invoice = getInvoiceByIdUseCase.get(invoiceId)

        if (user.id != invoice.user.id) {
            unauthorizedError(message = "User has no access to this resource")
        }

        repository.deleteByUUID(UUID.fromString(invoiceId))
    }

}