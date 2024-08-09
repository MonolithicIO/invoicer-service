package utils.password.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import utils.password.PasswordEncryption
import utils.password.PasswordEncryptionImpl
import utils.password.PasswordValidator
import utils.password.PasswordValidatorImpl

val utilPasswordDi = DI.Module("util-password") {
    bindProvider<PasswordEncryption> { PasswordEncryptionImpl() }
    bindProvider<PasswordValidator> { PasswordValidatorImpl() }
}