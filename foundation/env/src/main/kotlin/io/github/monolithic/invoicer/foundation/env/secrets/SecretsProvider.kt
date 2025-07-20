package io.github.monolithic.invoicer.foundation.env.secrets

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
    KAFKA_BOOTSTRAP,
    FIREBASE_ID,
    GCP_PROJECT_ID,
    BUCKET_PDFS
}

