package io.github.alaksion.invoicer.foundation.storage.remote

interface FileUploader {
    suspend fun uploadFile(
        localFilePath: String,
        fileName: String
    ): String
}