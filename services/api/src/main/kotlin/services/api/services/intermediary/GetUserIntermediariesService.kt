package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import java.util.UUID

interface GetUserIntermediariesService {
    suspend fun execute(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>
}