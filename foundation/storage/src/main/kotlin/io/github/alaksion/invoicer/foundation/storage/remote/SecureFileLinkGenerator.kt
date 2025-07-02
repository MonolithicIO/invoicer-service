package io.github.alaksion.invoicer.foundation.storage.remote

interface SecureFileLinkGenerator {
    suspend fun generateLink(
        fileKey: String,
        durationInHours: Int
    ): String
}
