package io.github.alaksion.invoicer.services.invoice

import io.github.alaksion.invoicer.services.company.GetCompanyDetailsService
import java.util.*
import repository.InvoiceRepository
import io.github.alaksion.invoicer.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

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
