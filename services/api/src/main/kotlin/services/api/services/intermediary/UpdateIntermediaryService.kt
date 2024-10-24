package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import models.intermediary.UpdateIntermediaryModel

interface UpdateIntermediaryService {
    suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: String,
        intermediaryId: String
    ): IntermediaryModel
}