package services.api.services.intermediary

interface DeleteIntermediaryService {
    suspend fun execute(
        beneficiaryId: String,
        userId: String
    )
}