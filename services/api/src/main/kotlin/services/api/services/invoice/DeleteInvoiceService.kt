package services.api.services.invoice

import services.api.repository.InvoiceRepository
import services.api.services.user.GetUserByIdService
import utils.exceptions.unauthorizedResourceError
import java.util.*

interface DeleteInvoiceService {
    suspend fun delete(
        invoiceId: String,
        userId: String
    )
}

internal class DeleteInvoiceServiceImpl(
    private val getInvoiceByIdService: GetInvoiceByIdService,
    private val getUserByIdUseCase: GetUserByIdService,
    private val repository: InvoiceRepository
) : DeleteInvoiceService {

    override suspend fun delete(invoiceId: String, userId: String) {
        val user = getUserByIdUseCase.get(userId)
        val invoice = getInvoiceByIdService.get(
            id = invoiceId,
            userId = userId
        )

        if (user.id != invoice.user.id) {
            unauthorizedResourceError()
        }

        repository.deleteByUUID(UUID.fromString(invoiceId))
    }

}