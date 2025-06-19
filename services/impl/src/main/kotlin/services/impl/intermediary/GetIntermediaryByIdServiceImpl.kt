package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.IntermediaryRepository
import services.api.services.intermediary.GetIntermediaryByIdService
import utils.exceptions.http.notFoundError
import utils.exceptions.http.forbiddenError
import java.util.*

internal class GetIntermediaryByIdServiceImpl(
    private val repository: IntermediaryRepository
) : GetIntermediaryByIdService {

    override suspend fun get(intermediaryId: UUID, userId: UUID): IntermediaryModel {
        val intermediary = repository.getById(
            intermediaryId = intermediaryId,
        )

        if (intermediary == null) notFoundError("Intermediary not found")

        if (intermediary.userId != userId) forbiddenError()

        return intermediary
    }
}