package services.api.services.beneficiary

import services.api.repository.BeneficiaryRepository
import services.api.repository.InvoiceRepository
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.httpError
import java.util.UUID

interface DeleteBeneficiaryService {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}

internal class DeleteBeneficiaryServiceImpl(
    private val getBeneficiaryByIdService: GetBeneficiaryByIdService,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteBeneficiaryService {

    override suspend fun execute(beneficiaryId: String, userId: String) {
        getUserByIdUseCase.get(userId)

        getBeneficiaryByIdService.get(
            beneficiaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByBeneficiaryId(
                beneficiaryId = UUID.fromString(beneficiaryId),
                userId = UUID.fromString(userId)
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete beneficiary with invoices associated", code = HttpCode.Conflict)
        }

        beneficiaryRepository.delete(
            beneficiaryId = UUID.fromString(beneficiaryId),
            userId = UUID.fromString(userId)
        )
    }
}