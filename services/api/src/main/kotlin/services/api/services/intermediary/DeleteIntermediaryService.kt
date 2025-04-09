package services.api.services.intermediary

import java.util.*

interface DeleteIntermediaryService {
    suspend fun execute(
        beneficiaryId: UUID,
        userId: UUID
    )
}