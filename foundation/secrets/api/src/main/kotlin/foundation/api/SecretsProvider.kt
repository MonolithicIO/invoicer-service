package foundation.api

import io.github.cdimascio.dotenv.dotenv

interface SecretsProvider {
    fun getSecret(key: SecretKeys): String
}

enum class SecretKeys {
    DB_NAME,
    DB_PASSWORD,
    DB_USERNAME
}

internal object SecretsProviderImpl : SecretsProvider {

    override fun getSecret(key: SecretKeys): String {
        val env = dotenv()

        return when(key) {
            SecretKeys.DB_NAME -> env["DB_NAME"]
            SecretKeys.DB_PASSWORD -> env["DB_PASSWORD"]
            SecretKeys.DB_USERNAME -> env["DB_USERNAME"]
        }
    }
}