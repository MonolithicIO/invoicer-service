package services.api.services.intermediary

import services.api.repository.IntermediaryRepository
import services.api.repository.InvoiceRepository
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.httpError
import java.util.UUID

interface DeleteIntermediaryService {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}

internal class DeleteIntermediaryServiceImpl(
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val intermediaryRepo: IntermediaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteIntermediaryService {

    override suspend fun execute(beneficiaryId: String, userId: String) {
        getUserByIdUseCase.get(userId)

        getIntermediaryByIdService.get(
            intermediaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByIntermediaryId(
                intermediaryId = UUID.fromString(beneficiaryId),
                userId = UUID.fromString(userId)
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete intermediary with invoices associated", code = HttpCode.Conflict)
        }

        intermediaryRepo.delete(
            intermediaryId = UUID.fromString(beneficiaryId),
            userId = UUID.fromString(userId)
        )
    }
}