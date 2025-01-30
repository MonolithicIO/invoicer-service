package foundation.impl.di

import foundation.impl.EnvSecretsProvider
import foundation.impl.SecretsProvider
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val secretsModule = DI.Module("secrets") {
    bindProvider<SecretsProvider> { EnvSecretsProvider(environment = instance()) }
}