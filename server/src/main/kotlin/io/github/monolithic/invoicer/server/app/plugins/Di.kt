package io.github.monolithic.invoicer.server.app.plugins

import io.github.monolithic.invoicer.foundation.cache.di.cacheDiModule
import io.github.monolithic.invoicer.foundation.password.di.utilPasswordDi
import io.github.monolithic.invoicer.foundation.qrcode.di.qrCodeModule
import io.github.monolithic.invoicer.consumers.di.consumersDiModule
import io.github.monolithic.invoicer.foundation.authentication.di.utilsAuthenticationModule
import io.github.monolithic.invoicer.foundation.env.di.invoicerEnvironmentDiModule
import io.github.monolithic.invoicer.foundation.log.di.logDiModule
import io.github.monolithic.invoicer.foundation.messaging.di.messagingDiModule
import io.github.monolithic.invoicer.foundation.storage.di.storageDiModule
import io.github.monolithic.invoicer.utils.di.utilDiModule
import io.ktor.server.application.Application
import org.kodein.di.ktor.di
import io.github.monolithic.invoicer.repository.di.repositoryModule
import io.github.monolithic.invoicer.services.di.servicesImplModule

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
