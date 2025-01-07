package foundation.api

import io.github.cdimascio.dotenv.dotenv

internal object DotEnvSecretProvider : SecretsProvider {

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
        }
    }
}