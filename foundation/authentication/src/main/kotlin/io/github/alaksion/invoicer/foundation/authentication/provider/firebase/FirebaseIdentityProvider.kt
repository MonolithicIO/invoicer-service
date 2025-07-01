package io.github.alaksion.invoicer.foundation.authentication.provider.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProvider
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProviderError
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProviderResult
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import io.github.alaksion.invoicer.utils.annotations.IgnoreCoverage
import java.io.FileInputStream
import kotlin.io.path.Path

@IgnoreCoverage
internal class FirebaseIdentityProvider(
    private val logger: Logger,
    private val secretsProvider: SecretsProvider
) : IdentityProvider {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override suspend fun getGoogleIdentity(token: String): IdentityProviderResult {
        return runCatching {
            firebaseAuth.verifyIdToken(token)
        }.fold(
            onSuccess = { account ->
                logger.log(
                    type = FirebaseIdentityProvider::class,
                    message = "Firebase Token verified successfully",
                    level = LogLevel.Debug
                )
                IdentityProviderResult.Success(
                    email = account.email,
                    providerUuid = account.uid
                )
            },
            onFailure = { error ->
                logger.log(
                    type = FirebaseIdentityProvider::class,
                    message = "Firebase Token verification failed",
                    level = LogLevel.Error,
                    throwable = error
                )
                if (error is FirebaseAuthException) {
                    IdentityProviderResult.Error(getError(error))
                } else {
                    IdentityProviderResult.Error(IdentityProviderError.UNKNOWN_ERROR)
                }
            }
        )
    }

    override fun initialize() {
        runCatching {
            val projectId = secretsProvider.getSecret(SecretKeys.FIREBASE_ID)
            val configFilePath =
                Path("").toAbsolutePath().toString() + "/etc/invoicer/configs/firebase-credentials.json"

            val configFile = FileInputStream(configFilePath)

            val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(configFile))
                .setDatabaseUrl("https://${projectId}.firebaseio.com/")
                .build()

            FirebaseApp.initializeApp(options)
        }.onFailure {
            logger.log(
                type = FirebaseIdentityProvider::class,
                message = "Firebase Admin SDK initialization failed, expect Firebase features to not work",
                throwable = it,
                level = LogLevel.Error
            )
        }
    }

    private fun getError(throwable: FirebaseAuthException): IdentityProviderError {
        return when (throwable.authErrorCode) {
            AuthErrorCode.EXPIRED_ID_TOKEN, AuthErrorCode.EXPIRED_SESSION_COOKIE -> IdentityProviderError.EXPIRED_TOKEN
            AuthErrorCode.INVALID_ID_TOKEN, AuthErrorCode.REVOKED_ID_TOKEN -> IdentityProviderError.INVALID_TOKEN
            AuthErrorCode.USER_NOT_FOUND, AuthErrorCode.USER_DISABLED, AuthErrorCode.EMAIL_NOT_FOUND -> IdentityProviderError.USER_NOT_FOUND
            else -> IdentityProviderError.UNKNOWN_ERROR
        }
    }
}