package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel

interface GetUserBeneficiariesService {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel>
}