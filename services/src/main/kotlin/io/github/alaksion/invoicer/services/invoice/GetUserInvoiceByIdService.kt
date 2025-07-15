package io.github.alaksion.invoicer.services.invoice

import io.github.alaksion.invoicer.services.company.GetCompanyDetailsService
import java.util.*
import models.invoice.InvoiceModel
import repository.InvoiceRepository
import io.github.alaksion.invoicer.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

interface GetUserInvoiceByIdService {
    suspend fun get(
        invoiceId: UUID,
        companyId: UUID,
        userId: UUID,
    ): InvoiceModel
}

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

        if (user.id != company.user.id) forbiddenError()

        val invoice = repository.getById(invoiceId) ?: notFoundError("Invoice not found")

        if (invoice.company.id != company.id) forbiddenError()

        return invoice
    }
}
