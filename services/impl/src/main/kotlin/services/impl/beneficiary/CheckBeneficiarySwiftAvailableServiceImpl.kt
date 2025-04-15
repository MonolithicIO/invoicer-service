package services.impl.beneficiary

import repository.BeneficiaryRepository
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import java.util.*

internal class CheckBeneficiarySwiftAvailableServiceImpl(
    private val repository: BeneficiaryRepository
) : CheckBeneficiarySwiftAvailableService {

    override suspend fun execute(swift: String, userId: UUID): Boolean {
        val response =
            repository.getBySwift(
                userId = userId,
                swift = swift
            ) != null

        return response
    }
}