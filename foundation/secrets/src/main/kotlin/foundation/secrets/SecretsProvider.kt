package foundation.secrets

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
    REDIS_HOST,
    REDIS_PORT,
    REDIS_TTL,
    MIN_IO_KEY,
    MIN_IO_SECRET_KEY,
    MIN_IO_URL,
    MIN_IO_BUCKET
}

