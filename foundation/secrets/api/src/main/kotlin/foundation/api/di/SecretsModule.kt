package foundation.api.di

import foundation.api.SecretsProvider
import foundation.api.SecretsProviderImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider

val secretsModule = DI.Module("secrets") {
    bindProvider<SecretsProvider> { SecretsProviderImpl }
}