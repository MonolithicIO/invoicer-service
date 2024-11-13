package foundation.authentication.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import foundation.authentication.api.AuthTokenGenerator
import foundation.authentication.api.AuthTokenManager
import foundation.authentication.api.AuthTokenManagerImpl
import foundation.authentication.api.jwt.InvoicerJwtVerifier
import foundation.authentication.api.jwt.InvoicerJwtVerifierImpl
import foundation.authentication.api.jwt.JwtTokenGenerator

val utilsAuthenticationModule = DI.Module("utils-authentication") {

    bindProvider<AuthTokenManager> {
        AuthTokenManagerImpl(
            tokenGenerator = instance(tag = AuthTokenGenerator.Tags.Jwt)
        )
    }

    bindProvider<AuthTokenGenerator>(tag = AuthTokenGenerator.Tags.Jwt) {
        JwtTokenGenerator(
            dateProvider = instance(),
            secretsProvider = instance(),
        )
    }

    bindProvider<InvoicerJwtVerifier> {
        InvoicerJwtVerifierImpl(
            secretsProvider = instance()
        )
    }
}