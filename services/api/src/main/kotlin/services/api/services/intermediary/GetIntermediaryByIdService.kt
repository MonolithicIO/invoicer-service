package services.api.services.intermediary

import models.intermediary.IntermediaryModel

interface GetIntermediaryByIdService {
    suspend fun get(intermediaryId: String, userId: String): IntermediaryModel
}