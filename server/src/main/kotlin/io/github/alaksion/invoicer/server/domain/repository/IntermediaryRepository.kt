package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import java.util.UUID

interface IntermediaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        beneficiaryId: UUID
    )

    suspend fun getById(
        userId: UUID,
        beneficiaryId: UUID
    ): IntermediaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel?

    suspend fun getAll(
        userId: UUID
    ): List<IntermediaryModel>
}