package datasource.api.database

import datasource.api.model.intermediary.CreateIntermediaryData
import datasource.api.model.intermediary.UpdateIntermediaryData
import models.intermediary.IntermediaryModel
import java.util.*

interface IntermediaryDatabaseSource {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryData
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
        model: UpdateIntermediaryData
    ): IntermediaryModel
}