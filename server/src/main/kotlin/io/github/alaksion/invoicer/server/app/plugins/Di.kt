package io.github.alaksion.invoicer.server.app.plugins

import foundation.cache.di.cacheDiModule
import foundation.password.di.utilPasswordDi
import foundation.qrcode.di.qrCodeModule
import io.github.alaksion.invoicer.consumers.di.consumersDiModule
import io.github.alaksion.invoicer.foundation.authentication.di.utilsAuthenticationModule
import io.github.alaksion.invoicer.foundation.env.di.invoicerEnvironmentDiModule
import io.github.alaksion.invoicer.foundation.log.di.logDiModule
import io.github.alaksion.invoicer.foundation.messaging.di.messagingDiModule
import io.github.alaksion.invoicer.foundation.storage.di.storageDiModule
import io.github.alaksion.invoicer.utils.di.utilDiModule
import io.ktor.server.application.Application
import org.kodein.di.ktor.di
import repository.di.repositoryModule
import io.github.alaksion.invoicer.services.di.servicesImplModule

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsAuthenticationModule)
        import(servicesImplModule)
        import(repositoryModule)
        import(invoicerEnvironmentDiModule)
        import(cacheDiModule)
        import(qrCodeModule)
        import(utilDiModule)
        import(storageDiModule)
        import(messagingDiModule)
        import(consumersDiModule)
        import(logDiModule)
    }
}
