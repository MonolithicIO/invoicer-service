import foundation.api.SecretKeys
import foundation.api.SecretsProvider
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun connect(
        secretsProvider: SecretsProvider
    ) {
        Database.connect(
            url = secretsProvider.getSecret(SecretKeys.DB_URL),
            driver = secretsProvider.getSecret(SecretKeys.DB_DRIVER),
            user = secretsProvider.getSecret(SecretKeys.DB_USERNAME),
            password = secretsProvider.getSecret(SecretKeys.DB_PASSWORD),
        )
    }
}