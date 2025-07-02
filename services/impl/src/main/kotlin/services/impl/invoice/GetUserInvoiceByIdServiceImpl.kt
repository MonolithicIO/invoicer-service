package services.impl.invoice

import java.util.*
import models.invoice.InvoiceModel
import repository.InvoiceRepository
import services.api.services.company.GetCompanyDetailsService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

internal class GetUserInvoiceByIdServiceImpl(
    private val repository: InvoiceRepository,
    private val getCompanyDetailsService: GetCompanyDetailsService,
    private val getUserService: GetUserByIdService
) : GetUserInvoiceByIdService {

    override suspend fun get(
        invoiceId: UUID,
        companyId: UUID,
        userId: UUID,
    ): InvoiceModel {
        val user = getUserService.get(userId)
        val company = getCompanyDetailsService.get(companyId) ?: notFoundError("Company not found")

        if (user.id != company.id) forbiddenError()

        val invoice = repository.getById(invoiceId) ?: notFoundError("Invoice not found")

        if (invoice.company.id != company.id) forbiddenError()

        return invoice
    }
}
