package services.api.services.intermediary

import java.util.*

interface CheckIntermediarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: UUID
    ): Boolean
}