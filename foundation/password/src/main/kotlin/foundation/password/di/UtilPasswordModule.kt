package foundation.password.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import foundation.password.PasswordEncryption
import foundation.password.PasswordEncryptionImpl
import foundation.password.PasswordValidator
import foundation.password.PasswordValidatorImpl

val utilPasswordDi = DI.Module("util-password") {
    bindProvider<PasswordEncryption> { PasswordEncryptionImpl() }
    bindProvider<PasswordValidator> { PasswordValidatorImpl() }
}