package io.github.alaksion.invoicer.foundation.storage.minIO

import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.FileUploader

internal class MinIOFileUploader(
    private val secretsProvider: SecretsProvider
) : FileUploader {

    override suspend fun uploadFile(filePath: String) {

    }
}