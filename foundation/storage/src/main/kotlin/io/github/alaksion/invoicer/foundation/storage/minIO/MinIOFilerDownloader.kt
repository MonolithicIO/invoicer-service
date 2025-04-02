package io.github.alaksion.invoicer.foundation.storage.minIO

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.FileDownloader
import io.minio.DownloadObjectArgs
import io.minio.MinioClient
import kotlin.io.path.Path

internal class MinIOFilerDownloader(
    private val secretsProvider: SecretsProvider
) : FileDownloader {

    override suspend fun downloadFile(fileKey: String): String {
        val client = MinioClient.builder()
            .endpoint(secretsProvider.getSecret(SecretKeys.MIN_IO_URL))
            .credentials(
                secretsProvider.getSecret(SecretKeys.MIN_IO_KEY),
                secretsProvider.getSecret(SecretKeys.MIN_IO_SECRET_KEY)
            )
            .build()

        val path = Path("").toAbsolutePath().toString() + "temp/downloads/$fileKey"

        val downloadProps = DownloadObjectArgs.Builder()
            .filename(fileKey)
            .bucket(secretsProvider.getSecret(SecretKeys.MIN_IO_BUCKET))
            .filename(path)
            .build()

        client.downloadObject(downloadProps)

        return path
    }
}