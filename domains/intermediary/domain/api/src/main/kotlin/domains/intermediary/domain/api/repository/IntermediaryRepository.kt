package domains.intermediary.domain.api.repository

import domains.intermediary.domain.api.model.CreateIntermediaryModel
import domains.intermediary.domain.api.model.IntermediaryModel
import domains.intermediary.domain.api.model.UpdateIntermediaryModel
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