package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import java.util.*

interface GetIntermediaryByIdService {
    suspend fun get(intermediaryId: UUID, userId: UUID): IntermediaryModel
}