package io.github.alaksion.foundation.identity.provider.firebase

import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.IdentityProviderResult

internal class FirebaseIdentityProvider: IdentityProvider {
    override suspend fun extractEmail(token: String): IdentityProviderResult {
        TODO("Not yet implemented")
    }
}