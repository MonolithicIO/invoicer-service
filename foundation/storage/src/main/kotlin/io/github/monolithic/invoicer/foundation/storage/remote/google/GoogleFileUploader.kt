package io.github.monolithic.invoicer.foundation.storage.remote.google

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.storage.remote.FileUploader
import java.io.FileInputStream
import java.nio.file.Paths

internal class GoogleFileUploader(
    private val secretsProvider: SecretsProvider,
    private val logger: Logger
) : FileUploader {

    override suspend fun uploadFile(localFilePath: String, fileName: String): String {
        logger.log(
            type = GoogleFileUploader::class,
            level = LogLevel.Debug,
            message = "Starting file to Google Cloud Storage: $localFilePath as $fileName"
        )

        val credentialsPath = FileInputStream(secretsProvider.getSecret(SecretKeys.GCP_CREDENTIALS_PATH))
        val projectId = secretsProvider.getSecret(SecretKeys.GCP_PROJECT_ID)
        val bucketName = secretsProvider.getSecret(SecretKeys.BUCKET_PDFS)

        val storage = StorageOptions.newBuilder()
            .setProjectId(projectId)
            .setCredentials(GoogleCredentials.fromStream(credentialsPath))
            .build().service
        val blobId = BlobId.of(bucketName, fileName)
        val blobInfo = BlobInfo.newBuilder(blobId).build()

        // Optional: set a generation-match precondition to avoid potential race
        // conditions and data corruptions. The request returns a 412 error if the
        // preconditions are not met.
        val precondition: Storage.BlobWriteOption =
            if (storage.get(bucketName, fileName) == null) {
                // For a target object that does not yet exist, set the DoesNotExist precondition.
                // This will cause the request to fail if the object is created before the request runs.
                Storage.BlobWriteOption.doesNotExist()
            } else {
                // If the destination already exists in your bucket, instead set a generation-match
                // precondition. This will cause the request to fail if the existing object's generation
                // changes before the request runs.
                Storage.BlobWriteOption.generationMatch(
                    storage.get(bucketName, fileName)
                        .generation
                )
            }

        return runCatching {
            storage.createFrom(blobInfo, Paths.get(localFilePath), precondition);
        }.fold(
            onSuccess = {
                logger.log(
                    type = GoogleFileUploader::class,
                    level = LogLevel.Debug,
                    message = "Successfully uploaded file to Google Cloud Storage: $localFilePath as $fileName",
                )
                blobInfo.name
            },
            onFailure = {
                logger.log(
                    type = GoogleFileUploader::class,
                    level = LogLevel.Error,
                    message = "Failed to upload file to Google Cloud Storage: ${it.message}",
                    throwable = it
                )
                throw it
            }
        )
    }
}