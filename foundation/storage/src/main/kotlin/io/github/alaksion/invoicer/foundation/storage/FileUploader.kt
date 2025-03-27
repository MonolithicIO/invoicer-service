package io.github.alaksion.invoicer.foundation.storage

interface FileUploader {
    suspend fun uploadFile(
        filePath: String
    )
}