package services.api.services.beneficiary

interface DeleteBeneficiaryService {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}