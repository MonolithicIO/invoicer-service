package io.github.monolithic.invoicer.foundation.authentication.token

interface AuthTokenVerifier {
    fun verify(token: String): String?
}
