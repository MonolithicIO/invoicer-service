package io.github.monolithic.invoicer.foundation.storage.remote

interface FileUploader {
    suspend fun uploadFile(
        localFilePath: String,
        fileName: String
    ): String
}
