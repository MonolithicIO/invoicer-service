package services.api.services.intermediary

import models.intermediary.IntermediaryModel

interface GetIntermediaryDetailsService {
    suspend fun getIntermediaryDetails(
        userId: String,
        intermediaryId: String
    ): IntermediaryModel
}