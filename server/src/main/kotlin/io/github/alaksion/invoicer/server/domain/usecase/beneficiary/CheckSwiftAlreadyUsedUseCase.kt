package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import java.util.UUID

interface CheckSwiftAlreadyUsedUseCase {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}

internal class CheckSwiftAvailableUseCaseImpl(
    private val repository: BeneficiaryRepository
) : CheckSwiftAlreadyUsedUseCase {

    override suspend fun execute(swift: String, userId: String): Boolean {
        val response =
            repository.getBySwift(
                userId = UUID.fromString(userId),
                swift = swift
            ) != null

        return response
    }
}