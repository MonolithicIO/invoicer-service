package io.github.alaksion.invoicer.server.app.plugins

import datasource.impl.di.datasourceModule
import foundation.authentication.impl.di.utilsAuthenticationModule
import foundation.cache.impl.di.cacheDiModule
import foundation.env.impl.invoicerEnvironmentDiModule
import foundation.impl.di.secretsModule
import foundation.qrcode.di.qrCodeModule
import foundation.validator.impl.di.validatorModule
import io.github.alaksion.invoicer.foundation.di.foundationDiModule
import io.ktor.server.application.*
import org.kodein.di.ktor.di
import repository.api.di.repositoryModule
import services.impl.di.servicesImplModule
import utils.password.impl.di.utilPasswordDi

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsAuthenticationModule)
        import(validatorModule)
        import(secretsModule)
        import(servicesImplModule)
        import(repositoryModule)
        import(invoicerEnvironmentDiModule)
        import(cacheDiModule)
        import(datasourceModule)
        import(qrCodeModule)
        import(foundationDiModule)
    }
}