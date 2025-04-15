package services.impl.invoice

import repository.api.InvoiceRepository
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.unauthorizedResourceError
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
            unauthorizedResourceError()
        }

        repository.delete(invoiceId)
    }

}