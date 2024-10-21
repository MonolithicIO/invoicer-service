package services.api.services.beneficiary

import models.beneficiary.CreateBeneficiaryModel

interface CreateBeneficiaryService {
    suspend fun create(
        model: CreateBeneficiaryModel,
        userId: String,
    ): String
}
