package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import java.util.UUID

interface GetUserIntermediariesUseCase {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>
}

internal class GetUserIntermediariesUseCaseImpl(
    private val repository: IntermediaryRepository
) : GetUserIntermediariesUseCase {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel> {
        return repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )
    }
}