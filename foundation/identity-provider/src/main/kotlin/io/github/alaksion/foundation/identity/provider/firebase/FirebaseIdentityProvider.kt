package io.github.alaksion.foundation.identity.provider.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthErrorCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.IdentityProviderError
import io.github.alaksion.foundation.identity.provider.IdentityProviderResult
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import io.github.alaksion.invoicer.utils.annotations.IgnoreCoverage

@IgnoreCoverage
internal class FirebaseIdentityProvider(
    private val firebaseAuth: FirebaseAuth,
    private val logger: Logger
) : IdentityProvider {

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
        FirebaseApp.initializeApp()
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