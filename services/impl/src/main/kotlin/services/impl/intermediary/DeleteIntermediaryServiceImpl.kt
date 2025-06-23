package services.impl.intermediary

import repository.IntermediaryRepository
import repository.InvoiceRepository
import services.api.services.intermediary.DeleteIntermediaryService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.user.GetUserByIdService
import java.util.*

internal class DeleteIntermediaryServiceImpl(
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val intermediaryRepo: IntermediaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteIntermediaryService {

    override suspend fun execute(beneficiaryId: UUID, userId: UUID) {
        getUserByIdUseCase.get(userId)

        getIntermediaryByIdService.get(
            intermediaryId = beneficiaryId,
            userId = userId
        )

        intermediaryRepo.delete(
            intermediaryId = beneficiaryId,
            userId = userId
        )
    }
}