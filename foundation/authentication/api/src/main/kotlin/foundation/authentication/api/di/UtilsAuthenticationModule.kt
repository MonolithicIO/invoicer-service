package foundation.authentication.api.di

import foundation.authentication.api.AuthTokenGenerator
import foundation.authentication.api.AuthTokenManager
import foundation.authentication.api.AuthTokenManagerImpl
import foundation.authentication.api.AuthTokenVerifier
import foundation.authentication.api.jwt.InvoicerJwtVerifierImpl
import foundation.authentication.api.jwt.JwtTokenGenerator
import org.kodein.di.DI
import org.kodein.di.bindProvider
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
            dateProvider = instance(),
            secretsProvider = instance(),
        )
    }

    bindProvider<AuthTokenVerifier>(tag = AuthTokenManager.Tags.Jwt) {
        InvoicerJwtVerifierImpl(
            secretsProvider = instance()
        )
    }
}