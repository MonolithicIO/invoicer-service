package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.HttpStatusCode
import utils.exceptions.httpError
import java.util.UUID

interface DeleteIntermediaryUseCase {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}

internal class DeleteIntermediaryUseCaseImpl(
    private val getIntermediaryByIdUseCase: GetIntermediaryByIdUseCase,
    private val intermediaryRepo: IntermediaryRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val invoiceRepository: InvoiceRepository
) : DeleteIntermediaryUseCase {

    override suspend fun execute(beneficiaryId: String, userId: String) {
        getUserByIdUseCase.get(userId)

        getIntermediaryByIdUseCase.get(
            intermediaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByIntermediaryId(
                intermediaryId = UUID.fromString(beneficiaryId),
                userId = UUID.fromString(userId)
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete intermediary with invoices associated", code = HttpStatusCode.Conflict)
        }

        intermediaryRepo.delete(
            intermediaryId = UUID.fromString(beneficiaryId),
            userId = UUID.fromString(userId)
        )
    }
}