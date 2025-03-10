package io.github.alaksion.invoicer.foundation.di

import io.github.alaksion.invoicer.foundation.events.QrCodeEventHandler
import io.github.alaksion.invoicer.foundation.events.QrCodeEventHandlerImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val foundationDiModule = DI.Module("foundationDiModule") {
    bindSingleton<QrCodeEventHandler> {
        QrCodeEventHandlerImpl()
    }
}