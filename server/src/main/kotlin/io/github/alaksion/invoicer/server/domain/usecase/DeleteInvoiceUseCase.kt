package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import java.util.*

interface DeleteInvoiceUseCase {
    suspend fun delete(id: String)
}

internal class DeleteInvoiceUseCaseImpl(
    private val getInvoiceByIdUseCase: GetInvoiceByIdUseCase,
    private val repository: InvoiceRepository
) : DeleteInvoiceUseCase {
    override suspend fun delete(id: String) {
        val invoice = getInvoiceByIdUseCase.get(id)

        repository.deleteByUUID(UUID.fromString(id))
    }

}