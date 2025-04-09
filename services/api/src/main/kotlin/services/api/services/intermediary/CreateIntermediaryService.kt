package services.api.services.intermediary

import models.intermediary.CreateIntermediaryModel
import java.util.UUID

interface CreateIntermediaryService {
    suspend fun create(
        model: CreateIntermediaryModel,
        userId: UUID,
    ): String
}