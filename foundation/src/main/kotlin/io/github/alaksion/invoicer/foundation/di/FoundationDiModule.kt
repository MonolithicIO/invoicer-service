package io.github.alaksion.invoicer.foundation.di

import io.github.alaksion.invoicer.foundation.events.QrCodeEventHandler
import io.github.alaksion.invoicer.foundation.events.QrCodeEventHandlerImpl
import io.github.alaksion.invoicer.foundation.validation.*
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val foundationDiModule = DI.Module("foundationDiModule") {
    bindSingleton<QrCodeEventHandler> { QrCodeEventHandlerImpl() }
    bindProvider<Clock> { Clock.System }
    bindProvider<EmailValidator> { EmailValidatorImpl }
    bindProvider<SwiftValidator> { SwiftValidatorImpl }
    bindProvider<IbanValidator> { IbanValidatorImpl }
}