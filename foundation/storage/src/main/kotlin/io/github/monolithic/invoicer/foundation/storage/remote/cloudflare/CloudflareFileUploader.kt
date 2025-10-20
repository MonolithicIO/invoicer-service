package io.github.monolithic.invoicer.foundation.storage.remote.cloudflare

import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.storage.remote.FileUploader
import java.net.URI
import java.nio.file.Paths
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest

internal class CloudflareFileUploader(
    private val secretsProvider: SecretsProvider,
    private val logger: Logger
) : FileUploader {

    private val s3ServiceClient by lazy {
        val credentials = AwsBasicCredentials.create(
            secretsProvider.getSecret(SecretKeys.S3_ACCESS_KEY),
            secretsProvider.getSecret(SecretKeys.S3_SECRET_KEY)
        )

        S3Client.builder()
            .endpointOverride(URI.create(secretsProvider.getSecret(SecretKeys.S3_ENDPOINT)))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            // CloudFlare ignores region but AWS sdk requires it to be set.
            .region(Region.US_EAST_1)
            .build()
    }

    override suspend fun uploadFile(localFilePath: String, fileName: String): String {
        logger.log(
            type = CloudflareFileUploader::class,
            level = LogLevel.Debug,
            message = "Starting file to CloudFlare R2 Storage: $localFilePath as $fileName"
        )

        val request = PutObjectRequest.builder()
            .bucket(secretsProvider.getSecret(SecretKeys.BUCKET_PDFS))
            .key(fileName)
            .build()

        runCatching {

        }

        return runCatching {
            s3ServiceClient.putObject(
                request,
                RequestBody.fromFile(Paths.get(localFilePath))
            )
        }.fold(
            onSuccess = {
                fileName
            },
            onFailure = {
                logger.log(
                    type = CloudflareFileUploader::class,
                    level = LogLevel.Error,
                    message = "Cloudflare file upload failed for file $fileName",
                    throwable = it
                )
                throw it
            }
        )
    }
}
