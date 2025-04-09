package services.api.services.beneficiary

import models.beneficiary.BeneficiaryModel
import java.util.*

interface GetBeneficiaryByIdService {
    suspend fun get(beneficiaryId: UUID, userId: UUID): BeneficiaryModel
}