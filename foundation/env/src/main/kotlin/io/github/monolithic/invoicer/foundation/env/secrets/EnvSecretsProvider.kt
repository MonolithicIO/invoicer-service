package io.github.monolithic.invoicer.foundation.env.secrets

import io.github.monolithic.invoicer.foundation.env.application.InvoicerEnvironment

internal class EnvSecretsProvider(
    private val environment: InvoicerEnvironment
) : SecretsProvider {

    @Suppress("CyclomaticComplexMethod")
    override fun getSecret(key: SecretKeys): String {
        return environment.getVariable(key.envName).orEmpty()
    }
}
