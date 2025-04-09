package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel
import java.util.UUID

interface GetBeneficiaryDetailsService {
    suspend fun getBeneficiaryDetails(
        userId: UUID,
        beneficiaryId: UUID
    ): BeneficiaryModel
}