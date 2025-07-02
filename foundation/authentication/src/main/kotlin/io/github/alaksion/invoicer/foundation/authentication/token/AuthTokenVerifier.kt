package io.github.alaksion.invoicer.foundation.authentication.token

interface AuthTokenVerifier {
    fun verify(token: String): String?
}
