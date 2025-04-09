package services.api.services.beneficiary

import models.beneficiary.CreateBeneficiaryModel
import java.util.*

interface CreateBeneficiaryService {
    suspend fun create(
        model: CreateBeneficiaryModel,
        userId: UUID,
    ): String
}
