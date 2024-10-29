package services.impl.intermediary

import models.intermediary.IntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.GetUserIntermediariesService
import services.api.services.user.GetUserByIdService
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal class GetUserIntermediariesServiceImpl(
    private val repository: IntermediaryRepository,
    private val getUserByIdService: GetUserByIdService
) : GetUserIntermediariesService {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel> {
        getUserByIdService.get(userId)

        val intermediaries = repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )

        if (intermediaries.any { it.userId != userId }) {
            unauthorizedResourceError()
        }

        return intermediaries
    }
}