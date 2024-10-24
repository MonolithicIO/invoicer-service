package services.impl.intermediary

import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import java.util.*

internal class CheckIntermediarySwiftAvailableServiceImpl(
    private val repository: IntermediaryRepository
) : CheckIntermediarySwiftAvailableService {

    override suspend fun execute(swift: String, userId: String): Boolean {
        val response =
            repository.getBySwift(
                userId = UUID.fromString(userId),
                swift = swift
            ) != null

        return response
    }
}