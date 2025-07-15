package io.github.monolithic.invoicer.services.invoice

import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import java.util.*
import io.github.monolithic.invoicer.models.invoice.InvoiceModel
import io.github.monolithic.invoicer.repository.InvoiceRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

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
