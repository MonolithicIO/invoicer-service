package io.github.monolithic.invoicer.foundation.env.di

import io.github.monolithic.invoicer.foundation.env.application.InvoicerEnvironment
import io.github.monolithic.invoicer.foundation.env.application.InvoicerEnvironmentImpl
import io.github.monolithic.invoicer.foundation.env.secrets.EnvSecretsProvider
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val invoicerEnvironmentDiModule = DI.Module("environment") {
    bindProvider<InvoicerEnvironment> {
        InvoicerEnvironmentImpl(
            application = instance()
        )
    }

    bindProvider<SecretsProvider> { EnvSecretsProvider(environment = instance()) }
}
