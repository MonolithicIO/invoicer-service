package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import java.util.*

interface UpdateIntermediaryService {
    suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: UUID,
        intermediaryId: UUID
    ): IntermediaryModel
}