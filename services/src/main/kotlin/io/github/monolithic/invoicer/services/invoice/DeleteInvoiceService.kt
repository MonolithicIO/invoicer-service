package io.github.monolithic.invoicer.services.invoice

import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import java.util.*
import io.github.monolithic.invoicer.repository.InvoiceRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface DeleteInvoiceService {
    suspend fun delete(
        invoiceId: UUID,
        userId: UUID
    )
}

internal class DeleteInvoiceServiceImpl(
    private val getUserByIdUseCase: GetUserByIdService,
    private val getCompanyByIdService: GetCompanyDetailsService,
    private val repository: InvoiceRepository
) : DeleteInvoiceService {

    override suspend fun delete(
        invoiceId: UUID,
        userId: UUID
    ) {
        val user = getUserByIdUseCase.get(userId)

        val invoice = repository.getById(id = invoiceId) ?: notFoundError("Invoice not found")

        val company = getCompanyByIdService.get(invoice.company.id) ?: notFoundError("Invoice company not found")

        if (company.user.id != user.id) forbiddenError()

        repository.delete(invoiceId)
    }
}
