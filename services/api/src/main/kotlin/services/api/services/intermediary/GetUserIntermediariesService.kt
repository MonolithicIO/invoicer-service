package services.api.services.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import java.util.*

interface GetUserIntermediariesService {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>
}

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