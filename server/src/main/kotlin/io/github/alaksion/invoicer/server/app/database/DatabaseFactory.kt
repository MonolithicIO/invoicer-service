package io.github.alaksion.invoicer.server.app.database

import io.github.alaksion.invoicer.foundation.env.secrets.SecretKeys
import io.github.alaksion.invoicer.foundation.env.secrets.SecretsProvider
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.connectDatabase() {
    val secrets by closestDI().instance<SecretsProvider>()

    Database.connect(
        url = secrets.getSecret(SecretKeys.DB_URL),
        driver = "org.postgresql.Driver",
        user = secrets.getSecret(SecretKeys.DB_USERNAME),
        password = secrets.getSecret(SecretKeys.DB_PASSWORD),
    )
}