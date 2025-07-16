package io.github.monolithic.invoicer.foundation.storage.remote

interface SecureFileLinkGenerator {
    suspend fun generateLink(
        fileKey: String,
        durationInHours: Int
    ): String
}
