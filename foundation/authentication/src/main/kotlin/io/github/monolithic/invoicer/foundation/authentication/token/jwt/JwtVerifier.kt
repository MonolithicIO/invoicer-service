package io.github.monolithic.invoicer.foundation.authentication.token.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenVerifier
import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider

internal class JwtVerifier(
    secretsProvider: SecretsProvider
) : AuthTokenVerifier {

    private val verifier = JWT
        .require(Algorithm.HMAC256(secretsProvider.getSecret(SecretKeys.JWT_SECRET)))
        .withAudience(secretsProvider.getSecret(SecretKeys.JWT_AUDIENCE))
        .withIssuer(secretsProvider.getSecret(SecretKeys.JWT_ISSUER))
        .build()


    override fun verify(token: String): String? {
        return runCatching {
            verifier.verify(token)
        }.fold(
            onSuccess = { it.getClaim(JwtConfig.USER_ID_CLAIM).asString() },
            onFailure = { null }
        )
    }
}
