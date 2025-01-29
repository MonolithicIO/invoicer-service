package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel

interface GetBeneficiaryDetailsService {
    suspend fun getBeneficiaryDetails(
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel
}