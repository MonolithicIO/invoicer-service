package services.impl.invoice

import repository.InvoiceRepository
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import java.util.*

internal class DeleteInvoiceServiceImpl(
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val getUserByIdUseCase: GetUserByIdService,
    private val repository: InvoiceRepository
) : DeleteInvoiceService {

    override suspend fun delete(invoiceId: UUID, userId: UUID) {
        val user = getUserByIdUseCase.get(userId)
        val invoice = getUserInvoiceByIdService.get(
            invoiceId = invoiceId,
            userId = userId
        )

        if (user.id != invoice.user.id) {
            forbiddenError()
        }

        repository.delete(invoiceId)
    }

}