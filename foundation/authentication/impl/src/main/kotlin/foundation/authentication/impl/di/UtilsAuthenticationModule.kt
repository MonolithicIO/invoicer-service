package foundation.authentication.impl.di

import foundation.authentication.impl.AuthTokenGenerator
import foundation.authentication.impl.AuthTokenManager
import foundation.authentication.impl.AuthTokenManagerImpl
import foundation.authentication.impl.AuthTokenVerifier
import foundation.authentication.impl.jwt.InvoicerJwtVerifierImpl
import foundation.authentication.impl.jwt.JwtTokenGenerator
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