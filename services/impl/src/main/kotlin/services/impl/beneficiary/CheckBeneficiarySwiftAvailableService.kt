package services.impl.beneficiary

import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import java.util.*

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