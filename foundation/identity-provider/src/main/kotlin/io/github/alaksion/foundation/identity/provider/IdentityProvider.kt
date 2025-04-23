package io.github.alaksion.foundation.identity.provider

interface IdentityProvider {
    suspend fun extractEmail(token: String): IdentityProviderResult
}

sealed interface IdentityProviderResult {
    data class Success(val email: String) : IdentityProviderResult
    data class Error(val error: IdentityProviderError) : IdentityProviderResult
}

enum class IdentityProviderError {
    INVALID_TOKEN,
    EXPIRED_TOKEN,
    USER_NOT_FOUND,
    UNKNOWN_ERROR
}