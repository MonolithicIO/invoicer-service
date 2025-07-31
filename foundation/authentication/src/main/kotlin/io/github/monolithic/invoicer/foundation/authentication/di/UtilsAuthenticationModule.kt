package io.github.monolithic.invoicer.foundation.authentication.di

import io.github.monolithic.invoicer.foundation.authentication.provider.IdentityProvider
import io.github.monolithic.invoicer.foundation.authentication.provider.firebase.FirebaseIdentityProvider
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenGenerator
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManagerImpl
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenVerifier
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.JwtTokenGenerator
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.JwtVerifier
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val utilsAuthenticationModule = DI.Module("utils-authentication") {

    bindProvider<AuthTokenManager> {
        AuthTokenManagerImpl(
            tokenGenerator = instance(tag = AuthTokenManager.Tags.Jwt),
            tokenVerifier = instance(tag = AuthTokenManager.Tags.Jwt)
        )
    }

    bindProvider<AuthTokenGenerator>(tag = AuthTokenManager.Tags.Jwt) {
        JwtTokenGenerator(
            clock = instance(),
            secretsProvider = instance(),
            uuidProvider = instance()
        )
    }

    bindProvider<AuthTokenVerifier>(tag = AuthTokenManager.Tags.Jwt) {
        JwtVerifier(
            secretsProvider = instance()
        )
    }

    bindSingleton<IdentityProvider> {
        FirebaseIdentityProvider(
            logger = instance(),
            secretsProvider = instance()
        )
    }
}
