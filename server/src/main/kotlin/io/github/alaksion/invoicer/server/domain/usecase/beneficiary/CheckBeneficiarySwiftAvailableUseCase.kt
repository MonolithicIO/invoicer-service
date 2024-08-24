package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import java.util.UUID

interface CheckBeneficiarySwiftAvailableUseCase {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}

internal class CheckBeneficiarySwiftAvailableUseCaseImpl(
    private val repository: BeneficiaryRepository
) : CheckBeneficiarySwiftAvailableUseCase {

    override suspend fun execute(swift: String, userId: String): Boolean {
        val response =
            repository.getBySwift(
                userId = UUID.fromString(userId),
                swift = swift
            ) != null

        return response
    }
}