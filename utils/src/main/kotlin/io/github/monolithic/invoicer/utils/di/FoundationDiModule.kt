package io.github.monolithic.invoicer.utils.di

import io.github.monolithic.invoicer.utils.events.QrCodeEventHandler
import io.github.monolithic.invoicer.utils.events.QrCodeEventHandlerImpl
import io.github.monolithic.invoicer.utils.validation.CountryCodeValidator
import io.github.monolithic.invoicer.utils.validation.CountryCodeValidatorImpl
import io.github.monolithic.invoicer.utils.validation.EmailValidator
import io.github.monolithic.invoicer.utils.validation.EmailValidatorImpl
import io.github.monolithic.invoicer.utils.validation.IbanValidator
import io.github.monolithic.invoicer.utils.validation.IbanValidatorImpl
import io.github.monolithic.invoicer.utils.validation.SwiftValidator
import io.github.monolithic.invoicer.utils.validation.SwiftValidatorImpl
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
    bindProvider<CountryCodeValidator> { CountryCodeValidatorImpl() }
}
