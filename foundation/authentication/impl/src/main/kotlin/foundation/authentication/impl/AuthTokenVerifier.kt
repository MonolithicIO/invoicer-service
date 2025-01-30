package foundation.authentication.impl

interface AuthTokenVerifier {
    fun verify(token: String): String?
}