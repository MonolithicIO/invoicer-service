package io.github.alaksion.invoicer.utils.di

import io.github.alaksion.invoicer.utils.events.QrCodeEventHandler
import io.github.alaksion.invoicer.utils.events.QrCodeEventHandlerImpl
import io.github.alaksion.invoicer.utils.validation.*
import kotlinx.datetime.Clock
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val utilDiModule = DI.Module("foundationDiModule") {
    bindSingleton<QrCodeEventHandler> { QrCodeEventHandlerImpl() }
    bindProvider<Clock> { Clock.System }
    bindProvider<EmailValidator> { EmailValidatorImpl }
    bindProvider<SwiftValidator> { SwiftValidatorImpl }
    bindProvider<IbanValidator> { IbanValidatorImpl }
}