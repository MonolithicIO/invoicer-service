package services.api.services.beneficiary

import models.beneficiary.UserBeneficiaries

interface GetUserBeneficiariesService {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): UserBeneficiaries
}