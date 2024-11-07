package foundation.api

import io.github.cdimascio.dotenv.dotenv

interface SecretsProvider {
    fun getSecret(key: SecretKeys): String
}

enum class SecretKeys {
    DB_PASSWORD,
    DB_USERNAME,
    DB_URL,
    DB_DRIVER,
    JWT_AUDIENCE,
    JWT_ISSUER,
    JWT_SECRET,
    JWT_REALM
}

internal object SecretsProviderImpl : SecretsProvider {

    override fun getSecret(key: SecretKeys): String {
        val env = dotenv()

        return when (key) {
            SecretKeys.DB_PASSWORD -> env["DB_PASSWORD"]
            SecretKeys.DB_USERNAME -> env["DB_USERNAME"]
            SecretKeys.JWT_AUDIENCE -> env["JWT_AUDIENCE"]
            SecretKeys.JWT_ISSUER -> env["JWT_ISSUER"]
            SecretKeys.JWT_SECRET -> env["JWT_SECRET"]
            SecretKeys.JWT_REALM -> env["JWT_REALM"]
            SecretKeys.DB_URL -> env["DB_URL"]
            SecretKeys.DB_DRIVER -> env["DB_DRIVER"]
        }
    }
}