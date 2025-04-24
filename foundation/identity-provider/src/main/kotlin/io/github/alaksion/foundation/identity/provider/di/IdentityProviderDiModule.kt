package io.github.alaksion.foundation.identity.provider.di

import io.github.alaksion.foundation.identity.provider.IdentityProvider
import io.github.alaksion.foundation.identity.provider.firebase.FirebaseIdentityProvider
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val identityProviderDiModule = DI.Module("identity-provider") {

    bindSingleton<IdentityProvider> {
        FirebaseIdentityProvider(
            logger = instance(),
            secretsProvider = instance()
        )
    }
}