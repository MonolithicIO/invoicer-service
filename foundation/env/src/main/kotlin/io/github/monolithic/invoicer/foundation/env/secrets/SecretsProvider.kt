package io.github.monolithic.invoicer.foundation.env.secrets

interface SecretsProvider {
    fun getSecret(key: SecretKeys): String
}

enum class SecretKeys(internal val envName: String) {
    DB_PASSWORD("database.password"),
    DB_USERNAME("database.username"),
    DB_URL("database.url"),
    JWT_AUDIENCE("jwt.audience"),
    JWT_ISSUER("jwt.issuer"),
    JWT_SECRET("jwt.secret"),
    JWT_REALM("jwt.realm"),
    REDIS_HOST("redis.host"),
    REDIS_PORT("redis.port"),
    KAFKA_BOOTSTRAP("kafka.bootstrap_servers"),
    FIREBASE_ID("firebase.project_id"),
    GCP_PROJECT_ID("file_upload.gcp_client_id"),
    BUCKET_PDFS("file_upload.bucket_pdfs"),
    GCP_CREDENTIALS_PATH("file_upload.gcp_credential_file"),
    FIREBASE_CREDENTIALS_PATH("firebase.service_account_file"),
}
