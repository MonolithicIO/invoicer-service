package services.api.services.beneficiary

import java.util.*

interface CheckBeneficiarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: UUID
    ): Boolean
}