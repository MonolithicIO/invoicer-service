package services.api.services.beneficiary

interface CheckBeneficiarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}