package services.impl.beneficiary

import models.beneficiary.BeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetBeneficiaryDetailsService
import services.api.services.user.GetUserByIdService
import utils.exceptions.notFoundError
import utils.exceptions.unauthorizedResourceError
import java.util.UUID

internal class GetBeneficiaryDetailsServiceServiceImpl(
    private val repository: BeneficiaryRepository,
    private val getUserService: GetUserByIdService,
) : GetBeneficiaryDetailsService {

    override suspend fun getBeneficiaryDetails(
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel {
        val user = getUserService.get(userId)

        val beneficiary = repository.getById(
            UUID.fromString(beneficiaryId)
        )

        if (beneficiary == null) {
            notFoundError("Beneficiary not found")
        }

        if (beneficiary.userId != user.id.toString()) {
            unauthorizedResourceError()
        }

        return BeneficiaryModel(
            name = beneficiary.name,
            iban = beneficiary.iban,
            swift = beneficiary.swift,
            bankName = beneficiary.bankName,
            bankAddress = beneficiary.bankAddress,
            userId = beneficiary.userId,
            id = beneficiary.id,
            createdAt = beneficiary.createdAt,
            updatedAt = beneficiary.updatedAt
        )
    }
}