package io.github.alaksion.invoicer.foundation.storage.remote

interface FileDownloader {
    suspend fun downloadFile(
        fileKey: String
    ): String
}