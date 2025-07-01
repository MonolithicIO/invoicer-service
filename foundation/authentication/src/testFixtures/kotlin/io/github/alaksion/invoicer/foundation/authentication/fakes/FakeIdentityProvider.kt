package io.github.alaksion.invoicer.foundation.authentication.fakes

import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProvider
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProviderResult
import java.util.*

class FakeIdentityProvider : IdentityProvider {

    var response: suspend () -> IdentityProviderResult = {
        IdentityProviderResult.Success(
            email = "sample@gmail.com",
            providerUuid = UUID.fromString("12345678-1234-5678-1234-123456789012").toString()
        )
    }

    override suspend fun getGoogleIdentity(token: String): IdentityProviderResult {
        return response()
    }

    override fun initialize() = Unit
}