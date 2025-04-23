package io.github.alaksion.foundation.identity.provider.firebase

import com.google.firebase.FirebaseApp
import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.IdentityProviderResult

internal class FirebaseIdentityProvider : IdentityProvider {

    override suspend fun extractEmail(token: String): IdentityProviderResult {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        FirebaseApp.initializeApp()
    }
}