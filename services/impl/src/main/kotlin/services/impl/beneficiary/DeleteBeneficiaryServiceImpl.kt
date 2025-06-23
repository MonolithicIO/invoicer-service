package services.impl.beneficiary

import repository.BeneficiaryRepository
import repository.InvoiceRepository
import services.api.services.beneficiary.DeleteBeneficiaryService
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.user.GetUserByIdService
import java.util.*

internal class DeleteBeneficiaryServiceImpl(
    private val getBeneficiaryByIdService: GetBeneficiaryByIdService,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val getUserByIdService: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteBeneficiaryService {

    override suspend fun execute(beneficiaryId: UUID, userId: UUID) {
        getUserByIdService.get(userId)

        getBeneficiaryByIdService.get(
            beneficiaryId = beneficiaryId,
            userId = userId
        )

        beneficiaryRepository.delete(
            beneficiaryId = beneficiaryId,
            userId = userId
        )
    }
}