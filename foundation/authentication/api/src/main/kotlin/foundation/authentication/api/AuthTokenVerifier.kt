package foundation.authentication.api

interface AuthTokenVerifier {
    fun verify(token: String): String?
}