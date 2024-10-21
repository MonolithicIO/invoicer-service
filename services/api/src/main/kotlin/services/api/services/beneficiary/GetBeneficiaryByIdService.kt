package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel

interface GetBeneficiaryByIdService {
    suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel
}