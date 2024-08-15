package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.UUID

interface GetIntermediaryByIdUseCase {
    suspend fun get(intermediaryId: String, userId: String): IntermediaryModel
}

internal class GetIntermediaryByIdUseCaseImpl(
    private val repository: IntermediaryRepository
) : GetIntermediaryByIdUseCase {

    override suspend fun get(intermediaryId: String, userId: String): IntermediaryModel {
        val intermediary = repository.getById(
            intermediaryId = UUID.fromString(intermediaryId),
        )

        if (intermediary == null) notFoundError("Beneficiary not found")

        if (intermediary.userId != userId) unauthorizedResourceError()

        return intermediary
    }
}