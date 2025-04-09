package services.api.services.beneficiary

import models.beneficiary.UserBeneficiaries
import java.util.UUID

interface GetUserBeneficiariesService {
    suspend fun execute(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries
}