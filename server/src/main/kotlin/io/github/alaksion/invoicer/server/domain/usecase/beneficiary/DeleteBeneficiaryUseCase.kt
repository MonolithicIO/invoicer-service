package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.HttpStatusCode
import utils.exceptions.httpError
import java.util.UUID

interface DeleteBeneficiaryUseCase {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}

internal class DeleteBeneficiaryUseCaseImpl(
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val invoiceRepository: InvoiceRepository
) : DeleteBeneficiaryUseCase {

    override suspend fun execute(beneficiaryId: String, userId: String) {
        getUserByIdUseCase.get(userId)

        getBeneficiaryByIdUseCase.get(
            beneficiaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByBeneficiaryId(
                beneficiaryId = UUID.fromString(beneficiaryId),
                userId = UUID.fromString(userId)
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete beneficiary with invoices associated", code = HttpStatusCode.Conflict)
        }

        beneficiaryRepository.delete(
            beneficiaryId = UUID.fromString(beneficiaryId),
            userId = UUID.fromString(userId)
        )
    }
}