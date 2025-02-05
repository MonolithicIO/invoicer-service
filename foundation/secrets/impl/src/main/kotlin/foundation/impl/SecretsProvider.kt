package foundation.impl

interface SecretsProvider {
    fun getSecret(key: SecretKeys): String
}

enum class SecretKeys {
    DB_PASSWORD,
    DB_USERNAME,
    DB_URL,
    JWT_AUDIENCE,
    JWT_ISSUER,
    JWT_SECRET,
    JWT_REALM,
    REDIS_URL
}

