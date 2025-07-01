package io.github.alaksion.invoicer.foundation.storage.remote.minIO

import io.github.alaksion.invoicer.foundation.env.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.storage.remote.FileDownloader
import io.minio.DownloadObjectArgs
import io.minio.MinioClient
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path


internal class MinIOFilerDownloader(
    private val secretsProvider: SecretsProvider
) : FileDownloader {

    override suspend fun downloadFile(fileKey: String): String {
        val client = MinioClient.builder()
            .endpoint("")
            .credentials(
                "",
                ""
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
            .bucket("")
            .filename(path)
            .build()

        client.downloadObject(downloadProps)

        return path
    }
}