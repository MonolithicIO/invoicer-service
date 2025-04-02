package io.github.alaksion.invoicer.foundation.storage

interface FileDownloader {
    suspend fun downloadFile(
        fileKey: String
    ): String
}