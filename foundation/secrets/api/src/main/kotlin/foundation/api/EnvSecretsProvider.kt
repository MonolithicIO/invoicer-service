package foundation.api

import foundation.env.InvoicerEnvironment

internal class EnvSecretsProvider(
    private val environment: InvoicerEnvironment
) : SecretsProvider {

    override fun getSecret(key: SecretKeys): String {
        val path = when (key) {
            SecretKeys.DB_PASSWORD -> "invoicer.db.password"
            SecretKeys.DB_USERNAME -> "invoicer.db.username"
            SecretKeys.DB_URL -> "invoicer.db.url"
            SecretKeys.JWT_AUDIENCE -> "invoicer.jwt.audience"
            SecretKeys.JWT_ISSUER -> "invoicer.jwt.issuer"
            SecretKeys.JWT_SECRET -> "invoicer.jwt.secret"
            SecretKeys.JWT_REALM -> "invoicer.jwt.realm"
        }

        return environment.getVariable(path).orEmpty()
    }
}