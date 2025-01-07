package io.github.alaksion.invoicer.server.app.plugins

import foundation.api.di.secretsModule
import foundation.authentication.api.di.utilsAuthenticationModule
import foundation.env.invoicerEnvironmentDiModule
import foundation.validator.api.di.validatorModule
import io.ktor.server.application.*
import org.kodein.di.bindSingleton
import org.kodein.di.ktor.di
import repository.api.di.repositoryModule
import services.impl.di.servicesImplModule
import utils.date.api.di.utilsDateModule
import utils.password.di.utilPasswordDi

fun Application.installDi() {
    di {
        bindSingleton<Application> {
            this@installDi
        }
        import(utilPasswordDi)
        import(utilsDateModule)
        import(utilsAuthenticationModule)
        import(validatorModule)
        import(secretsModule)
        import(servicesImplModule)
        import(repositoryModule)
        import(invoicerEnvironmentDiModule)
    }
}