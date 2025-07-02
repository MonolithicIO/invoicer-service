package services.impl.invoice

import java.util.*
import repository.InvoiceRepository
import services.api.services.company.GetCompanyDetailsService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

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
