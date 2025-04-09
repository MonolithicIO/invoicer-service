package services.impl.intermediary

import repository.api.repository.IntermediaryRepository
import repository.api.repository.InvoiceRepository
import services.api.services.intermediary.DeleteIntermediaryService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.httpError
import java.util.*

internal class DeleteIntermediaryServiceImpl(
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val intermediaryRepo: IntermediaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteIntermediaryService {

    override suspend fun execute(beneficiaryId: UUID, userId: UUID) {
        getUserByIdUseCase.get(userId.toString())

        getIntermediaryByIdService.get(
            intermediaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByIntermediaryId(
                intermediaryId = beneficiaryId,
                userId = userId
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete intermediary with invoices associated", code = HttpCode.Conflict)
        }

        intermediaryRepo.delete(
            intermediaryId = beneficiaryId,
            userId = userId
        )
    }
}