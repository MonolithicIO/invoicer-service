import foundation.api.SecretKeys
import foundation.api.SecretsProvider
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun connect(
        secretsProvider: SecretsProvider
    ) {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/${secretsProvider.getSecret(SecretKeys.DB_NAME)}",
            driver = "org.postgresql.Driver",
            user = secretsProvider.getSecret(SecretKeys.DB_USERNAME),
            password = secretsProvider.getSecret(SecretKeys.DB_PASSWORD),
        )
    }
}