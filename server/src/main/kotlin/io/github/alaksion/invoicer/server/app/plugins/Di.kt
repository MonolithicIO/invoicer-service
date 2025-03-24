package io.github.alaksion.invoicer.server.app.plugins

import datasource.impl.di.datasourceModule
import foundation.authentication.impl.di.utilsAuthenticationModule
import foundation.cache.impl.di.cacheDiModule
import foundation.env.invoicerEnvironmentDiModule
import foundation.secrets.di.secretsModule
import foundation.qrcode.di.qrCodeModule
import io.github.alaksion.invoicer.utils.di.utilDiModule
import io.ktor.server.application.*
import org.kodein.di.ktor.di
import repository.api.di.repositoryModule
import services.impl.di.servicesImplModule
import foundation.password.di.utilPasswordDi

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsAuthenticationModule)
        import(secretsModule)
        import(servicesImplModule)
        import(repositoryModule)
        import(invoicerEnvironmentDiModule)
        import(cacheDiModule)
        import(datasourceModule)
        import(qrCodeModule)
        import(utilDiModule)
    }
}