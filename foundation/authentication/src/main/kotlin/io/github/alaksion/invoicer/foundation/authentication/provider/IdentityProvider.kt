package io.github.alaksion.invoicer.foundation.authentication.provider

interface IdentityProvider {
    /**
     * Retrieves Google account from the identity provider using the provided token.
     *
     * @param token The token to be used for authentication.
     * */
    suspend fun getGoogleIdentity(token: String): IdentityProviderResult

    /**
     * Initializes the identity provider. This method should be called before using the identity provider.
     * Depending on the SDK being used this might be a non-op.
     * */
    fun initialize()
}

sealed interface IdentityProviderResult {
    data class Success(val email: String, val providerUuid: String) : IdentityProviderResult
    data class Error(val error: IdentityProviderError) : IdentityProviderResult
}

enum class IdentityProviderError {
    INVALID_TOKEN,
    EXPIRED_TOKEN,
    USER_NOT_FOUND,
    UNKNOWN_ERROR
}