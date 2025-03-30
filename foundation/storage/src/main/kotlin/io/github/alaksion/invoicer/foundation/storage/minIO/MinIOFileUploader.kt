package io.github.alaksion.invoicer.foundation.storage.minIO

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.FileUploader
import io.minio.MinioClient
import io.minio.UploadObjectArgs

internal class MinIOFileUploader(
    private val secretsProvider: SecretsProvider
) : FileUploader {

    override suspend fun uploadFile(
        localFilePath: String,
        fileName: String
    ): String {
        val client = MinioClient.builder()
            .endpoint(secretsProvider.getSecret(SecretKeys.MIN_IO_URL))
            .credentials(
                secretsProvider.getSecret(SecretKeys.MIN_IO_KEY),
                secretsProvider.getSecret(SecretKeys.MIN_IO_SECRET_KEY)
            )
            .build()

        val uploadPayload = UploadObjectArgs.builder()
            .bucket(secretsProvider.getSecret(SecretKeys.MIN_IO_BUCKET))
            .`object`(fileName)
            .filename(localFilePath)
            .build()

        val response = client.uploadObject(uploadPayload)

        return response.`object`()
    }
}