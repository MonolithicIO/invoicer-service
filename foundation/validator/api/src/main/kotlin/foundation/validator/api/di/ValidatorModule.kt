package foundation.validator.api.di

import foundation.validator.api.EmailValidator
import foundation.validator.api.EmailValidatorImpl
import foundation.validator.api.IbanValidator
import foundation.validator.api.IbanValidatorImpl
import foundation.validator.api.SwiftValidator
import foundation.validator.api.SwiftValidatorImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider

val validatorModule = DI.Module("validator_module") {
    bindProvider<EmailValidator> {
        EmailValidatorImpl
    }
    bindProvider<SwiftValidator> {
        SwiftValidatorImpl
    }
    bindProvider<IbanValidator> {
        IbanValidatorImpl
    }
}