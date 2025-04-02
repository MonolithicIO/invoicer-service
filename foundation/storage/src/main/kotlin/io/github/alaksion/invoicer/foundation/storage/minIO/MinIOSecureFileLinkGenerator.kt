package io.github.alaksion.invoicer.foundation.storage.minIO

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.SecureFileLinkGenerator
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import java.util.concurrent.TimeUnit


internal class MinIOSecureFileLinkGenerator(
    private val secretsProvider: SecretsProvider
) : SecureFileLinkGenerator {

    override suspend fun generateLink(fileKey: String, durationInHours: Int): String {
        val client = MinioClient.builder()
            .endpoint(secretsProvider.getSecret(SecretKeys.MIN_IO_URL))
            .credentials(
                secretsProvider.getSecret(SecretKeys.MIN_IO_KEY),
                secretsProvider.getSecret(SecretKeys.MIN_IO_SECRET_KEY)
            )
            .build()

        return client.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(secretsProvider.getSecret(SecretKeys.MIN_IO_BUCKET))
                .`object`(fileKey)
                .expiry(durationInHours, TimeUnit.HOURS)
                .build()
        )
    }
}