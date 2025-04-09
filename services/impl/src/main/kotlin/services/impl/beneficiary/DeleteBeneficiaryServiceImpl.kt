package services.impl.beneficiary

import utils.exceptions.http.HttpCode
import repository.api.repository.BeneficiaryRepository
import repository.api.repository.InvoiceRepository
import services.api.services.beneficiary.DeleteBeneficiaryService
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.httpError
import java.util.*

internal class DeleteBeneficiaryServiceImpl(
    private val getBeneficiaryByIdService: GetBeneficiaryByIdService,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val getUserByIdService: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteBeneficiaryService {

    override suspend fun execute(beneficiaryId: UUID, userId: UUID) {
        getUserByIdService.get(userId.toString())

        getBeneficiaryByIdService.get(
            beneficiaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByBeneficiaryId(
                beneficiaryId = beneficiaryId,
                userId = userId
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete beneficiary with invoices associated", code = HttpCode.Conflict)
        }

        beneficiaryRepository.delete(
            beneficiaryId = beneficiaryId,
            userId = userId
        )
    }
}