package services.api.repository

import services.api.model.intermediary.CreateIntermediaryModel
import services.api.model.intermediary.IntermediaryModel
import services.api.model.intermediary.UpdateIntermediaryModel
import java.util.UUID

interface IntermediaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        intermediaryId: UUID
    )

    suspend fun getById(
        intermediaryId: UUID
    ): IntermediaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>

    suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: UpdateIntermediaryModel
    ): IntermediaryModel
}