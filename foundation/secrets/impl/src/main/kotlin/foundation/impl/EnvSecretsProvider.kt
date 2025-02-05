package foundation.impl

import foundation.env.impl.InvoicerEnvironment

internal class EnvSecretsProvider(
    private val environment: InvoicerEnvironment
) : SecretsProvider {

    override fun getSecret(key: SecretKeys): String {
        val path = when (key) {
            SecretKeys.DB_PASSWORD -> "database.password"
            SecretKeys.DB_USERNAME -> "database.username"
            SecretKeys.DB_URL -> "database.url"
            SecretKeys.JWT_AUDIENCE -> "jwt.audience"
            SecretKeys.JWT_ISSUER -> "jwt.issuer"
            SecretKeys.JWT_SECRET -> "jwt.secret"
            SecretKeys.JWT_REALM -> "jwt.realm"
            SecretKeys.REDIS_URL -> "redis.url"
        }

        return environment.getVariable(path).orEmpty()
    }
}