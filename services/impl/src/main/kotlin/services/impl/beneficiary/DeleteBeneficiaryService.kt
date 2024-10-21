package services.impl.beneficiary

import repository.api.repository.BeneficiaryRepository
import repository.api.repository.InvoiceRepository
import services.api.services.beneficiary.DeleteBeneficiaryService
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.httpError
import java.util.*

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