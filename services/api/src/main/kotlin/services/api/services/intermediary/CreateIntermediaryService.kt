package services.api.services.intermediary

import models.intermediary.CreateIntermediaryModel

interface CreateIntermediaryService {
    suspend fun create(
        model: CreateIntermediaryModel,
        userId: String,
    ): String
}