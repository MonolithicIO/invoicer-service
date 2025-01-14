package foundation.api.di

import foundation.api.EnvSecretsProvider
import foundation.api.SecretsProvider
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val secretsModule = DI.Module("secrets") {
    bindProvider<SecretsProvider> { EnvSecretsProvider(environment = instance()) }
}