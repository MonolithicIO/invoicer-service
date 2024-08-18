package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import java.util.UUID

interface GetUserBeneficiariesUseCase {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel>
}

internal class GetUserBeneficiariesUseCaseImpl(
    private val repository: BeneficiaryRepository
) : GetUserBeneficiariesUseCase {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel> {
        return repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )
    }
}