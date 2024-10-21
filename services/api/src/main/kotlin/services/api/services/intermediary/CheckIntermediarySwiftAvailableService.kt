package services.api.services.intermediary

interface CheckIntermediarySwiftAvailableService {
    suspend fun execute(
        swift: String,
        userId: String
    ): Boolean
}