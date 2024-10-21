package services.impl.intermediary

import repository.api.repository.IntermediaryRepository
import java.util.*

interface CheckIntermediarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}

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