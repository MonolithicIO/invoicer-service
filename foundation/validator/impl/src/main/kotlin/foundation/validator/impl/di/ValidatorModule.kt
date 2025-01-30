package foundation.validator.impl.di

import foundation.validator.impl.EmailValidator
import foundation.validator.impl.EmailValidatorImpl
import foundation.validator.impl.IbanValidator
import foundation.validator.impl.IbanValidatorImpl
import foundation.validator.impl.SwiftValidator
import foundation.validator.impl.SwiftValidatorImpl
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