package services.api.services.beneficiary

import java.util.*

interface DeleteBeneficiaryService {
    suspend fun execute(
        beneficiaryId: UUID,
        userId: UUID
    )
}