package io.github.alaksion.invoicer.foundation.storage

interface SecureFileLinkGenerator {
    suspend fun generateLink(
        fileKey: String,
        durationInHours: Int
    ): String
}