package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel

interface UpdateBeneficiaryService {
    suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel
}