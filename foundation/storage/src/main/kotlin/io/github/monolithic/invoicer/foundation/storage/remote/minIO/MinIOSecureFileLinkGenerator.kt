package io.github.monolithic.invoicer.foundation.storage.remote.minIO

import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import java.util.concurrent.TimeUnit

@Suppress("UnusedPrivateProperty")
internal class MinIOSecureFileLinkGenerator(
    private val secretsProvider: SecretsProvider
) : SecureFileLinkGenerator {

    override suspend fun generateLink(fileKey: String, durationInHours: Int): String {
        val client = MinioClient.builder()
            .endpoint("")
            .credentials(
                "",
                ""
            )
            .build()

        return client.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket("")
                .`object`(fileKey)
                .expiry(durationInHours, TimeUnit.HOURS)
                .build()
        )
    }
}
