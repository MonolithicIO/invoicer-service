package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel
import java.util.*

interface UpdateBeneficiaryService {
    suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: UUID,
        beneficiaryId: UUID
    ): BeneficiaryModel
}