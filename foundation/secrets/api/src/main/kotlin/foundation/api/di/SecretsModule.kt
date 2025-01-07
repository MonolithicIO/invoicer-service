package foundation.api.di

import foundation.api.DotEnvSecretProvider
import foundation.api.SecretsProvider
import org.kodein.di.DI
import org.kodein.di.bindProvider

val secretsModule = DI.Module("secrets") {
    bindProvider<SecretsProvider> { DotEnvSecretProvider }
}