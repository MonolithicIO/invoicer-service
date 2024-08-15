package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.UUID

interface GetBeneficiaryByIdUseCase {
    suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel
}

internal class GetBeneficiaryByIdUseCaseImpl(
    private val repository: BeneficiaryRepository
) : GetBeneficiaryByIdUseCase {

    override suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel {
        val beneficiary = repository.getById(
            beneficiaryId = UUID.fromString(beneficiaryId),
        )

        if (beneficiary == null) notFoundError("Beneficiary not found")

        if (beneficiary.userId != userId) unauthorizedResourceError()

        return beneficiary
    }

}