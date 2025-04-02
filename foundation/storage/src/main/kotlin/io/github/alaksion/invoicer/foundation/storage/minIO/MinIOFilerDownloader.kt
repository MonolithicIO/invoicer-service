package io.github.alaksion.invoicer.foundation.storage.minIO

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.FileDownloader
import io.minio.DownloadObjectArgs
import io.minio.MinioClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting


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

        val path = Path("").toAbsolutePath().toString() + "/temp/downloads/$fileKey"

        val dirPath: Path = Paths.get(path).parent

        // Create path if not exists
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath)
        }

        val downloadProps = DownloadObjectArgs.Builder()
            .`object`(fileKey)
            .bucket(secretsProvider.getSecret(SecretKeys.MIN_IO_BUCKET))
            .filename(path)
            .build()

        client.downloadObject(downloadProps)

        return path
    }
}