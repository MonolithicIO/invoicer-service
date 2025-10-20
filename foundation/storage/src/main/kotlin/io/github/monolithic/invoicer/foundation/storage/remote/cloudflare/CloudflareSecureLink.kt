package io.github.monolithic.invoicer.foundation.storage.remote.cloudflare

import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import java.net.URI
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest

internal class CloudflareSecureLink(
    private val secretsProvider: SecretsProvider,
    private val logger: Logger
) : SecureFileLinkGenerator {

    private val urlSigner by lazy {
        val credentials = AwsBasicCredentials.create(
            secretsProvider.getSecret(SecretKeys.S3_ACCESS_KEY),
            secretsProvider.getSecret(SecretKeys.S3_SECRET_KEY)
        )

        S3Presigner.builder()
            .endpointOverride(URI.create(secretsProvider.getSecret(SecretKeys.S3_ENDPOINT)))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            // CloudFlare ignores region but AWS sdk requires it to be set.
            .region(Region.US_EAST_1)
            .build()
    }

    override suspend fun generateLink(fileKey: String, durationInHours: Int): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(secretsProvider.getSecret(SecretKeys.BUCKET_PDFS))
            .key(fileKey)
            .build()

        val presignedRequest = GetObjectPresignRequest.builder()
            .signatureDuration(durationInHours.hours.toJavaDuration())
            .getObjectRequest(getObjectRequest)
            .build()

        return runCatching {
            urlSigner.presignGetObject(presignedRequest)
        }.fold(
            onSuccess = {
                it.url().toString()
            },
            onFailure = {
                logger.log(
                    type = CloudflareSecureLink::class,
                    level = LogLevel.Error,
                    message = "Cloudflare secure link generation failed for file $fileKey",
                    throwable = it
                )
                throw it
            }
        )
    }
}