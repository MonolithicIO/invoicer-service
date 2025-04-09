package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import java.util.*

interface GetIntermediaryDetailsService {
    suspend fun getIntermediaryDetails(
        userId: UUID,
        intermediaryId: UUID
    ): IntermediaryModel
}