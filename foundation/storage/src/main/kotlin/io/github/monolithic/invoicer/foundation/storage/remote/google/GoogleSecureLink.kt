package io.github.monolithic.invoicer.foundation.storage.remote.google

import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import java.io.FileInputStream
import java.util.concurrent.TimeUnit


internal class GoogleSecureLink(
    private val secretsProvider: SecretsProvider
) : SecureFileLinkGenerator {

    override suspend fun generateLink(fileKey: String, durationInHours: Int): String {

        val projectId = secretsProvider.getSecret(SecretKeys.GCP_PROJECT_ID)
        val serviceAccount =
            FileInputStream(secretsProvider.getSecret(SecretKeys.GCP_SERVICE_ACCOUNT))
        val bucket = secretsProvider.getSecret(SecretKeys.BUCKET_PDFS)

        val storage: Storage = StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(ServiceAccountCredentials.fromStream(serviceAccount))
            .build().service

        // Define resource
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, fileKey)).build()

        val url = storage.signUrl(
            blobInfo,
            durationInHours.toMinutes(),
            TimeUnit.MINUTES,
            Storage.SignUrlOption.withV4Signature()
        )

        return url.toString()
    }

    private fun Int.toMinutes() = this * MINUTES_MULTIPLIER

    companion object {
        const val MINUTES_MULTIPLIER = 60L
    }
}
