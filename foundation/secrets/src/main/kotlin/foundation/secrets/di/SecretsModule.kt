package foundation.secrets.di

import foundation.secrets.EnvSecretsProvider
import foundation.secrets.SecretsProvider
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val secretsModule = DI.Module("secrets") {
    bindProvider<SecretsProvider> { EnvSecretsProvider(environment = instance()) }
}