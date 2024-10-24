package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.GetUserIntermediariesService
import java.util.*

internal class GetUserIntermediariesServiceImpl(
    private val repository: IntermediaryRepository
) : GetUserIntermediariesService {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel> {
        return repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )
    }
}