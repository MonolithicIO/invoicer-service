package io.github.monolithic.invoicer.foundation.storage.remote

interface FileDownloader {
    suspend fun downloadFile(
        fileKey: String
    ): String
}
