package services.api.services.intermediary

import models.intermediary.IntermediaryModel

interface GetUserIntermediariesService {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>
}