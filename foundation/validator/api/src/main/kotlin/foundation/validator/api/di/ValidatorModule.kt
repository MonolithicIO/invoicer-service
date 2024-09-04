package foundation.validator.api.di

import foundation.validator.api.EmailValidator
import foundation.validator.api.EmailValidatorImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider

val validatorModule = DI.Module("validator_module") {
    bindProvider<EmailValidator> {
        EmailValidatorImpl
    }
}