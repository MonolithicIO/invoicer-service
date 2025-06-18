package io.github.alaksion.invoicer.server.app.plugins

import foundation.authentication.impl.di.utilsAuthenticationModule
import foundation.cache.di.cacheDiModule
import foundation.env.invoicerEnvironmentDiModule
import foundation.password.di.utilPasswordDi
import foundation.qrcode.di.qrCodeModule
import foundation.secrets.di.secretsModule
import io.github.alaksion.foundation.identity.provider.di.identityProviderDiModule
import io.github.alaksion.invoicer.consumers.di.consumersDiModule
import io.github.alaksion.invoicer.foundation.log.di.logDiModule
import io.github.alaksion.invoicer.foundation.messaging.di.messagingDiModule
import io.github.alaksion.invoicer.foundation.storage.di.storageDiModule
import io.github.alaksion.invoicer.utils.di.utilDiModule
import io.ktor.server.application.*
import org.kodein.di.ktor.di
import repository.di.repositoryModule
import services.impl.di.servicesImplModule

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsAuthenticationModule)
        import(secretsModule)
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
        import(identityProviderDiModule)
    }
}