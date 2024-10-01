package services.api.services.beneficiary

import services.api.repository.BeneficiaryRepository
import java.util.UUID

interface CheckBeneficiarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}

internal class CheckBeneficiarySwiftAvailableServiceImpl(
    private val repository: BeneficiaryRepository
) : CheckBeneficiarySwiftAvailableService {

    override suspend fun execute(swift: String, userId: String): Boolean {
        val response =
            repository.getBySwift(
                userId = UUID.fromString(userId),
                swift = swift
            ) != null

        return response
    }
}