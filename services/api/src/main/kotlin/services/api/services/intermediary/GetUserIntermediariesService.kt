package services.api.services.intermediary

import services.api.model.intermediary.IntermediaryModel
import services.api.repository.IntermediaryRepository
import java.util.UUID

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