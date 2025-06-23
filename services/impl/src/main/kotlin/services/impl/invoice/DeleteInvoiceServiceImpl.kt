package services.impl.invoice

import repository.InvoiceRepository
import services.api.services.company.GetCompanyDetailsService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError
import java.util.*

internal class DeleteInvoiceServiceImpl(
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val getUserByIdUseCase: GetUserByIdService,
    private val getCompanyByIdService: GetCompanyDetailsService,
    private val repository: InvoiceRepository
) : DeleteInvoiceService {

    override suspend fun delete(
        invoiceId: UUID,
        userId: UUID
    ) {
        val user = getUserByIdUseCase.get(userId)

        val invoice = getUserInvoiceByIdService.get(invoiceId = invoiceId) ?: notFoundError("Invoice not found")

        val company = getCompanyByIdService.get(invoice.company.id) ?: notFoundError("Invoice company not found")

        if (company.user.id != user) forbiddenError()

        repository.delete(invoiceId)
    }
}