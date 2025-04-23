package io.github.alaksion.foundation.identity.provider.fakes

import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.IdentityProviderResult

class FakeIdentityProvider : IdentityProvider {

    var response: suspend () -> IdentityProviderResult = { IdentityProviderResult.Success("sample") }

    override suspend fun extractEmail(token: String): IdentityProviderResult {
        return response()
    }

    override fun initialize() = Unit
}