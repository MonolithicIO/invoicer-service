package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import java.util.UUID

interface CheckIntermediarySwiftAvailableUseCase {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}

internal class CheckIntermediarySwiftAvailableUseCaseImpl(
    private val repository: IntermediaryRepository
) : CheckIntermediarySwiftAvailableUseCase {

    override suspend fun execute(swift: String, userId: String): Boolean {
        val response =
            repository.getBySwift(
                userId = UUID.fromString(userId),
                swift = swift
            ) != null

        return response
    }
}