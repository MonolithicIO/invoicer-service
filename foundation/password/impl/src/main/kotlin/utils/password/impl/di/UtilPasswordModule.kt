package utils.password.impl.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import utils.password.impl.PasswordEncryption
import utils.password.impl.PasswordEncryptionImpl
import utils.password.impl.PasswordValidator
import utils.password.impl.PasswordValidatorImpl

val utilPasswordDi = DI.Module("util-password") {
    bindProvider<PasswordEncryption> { PasswordEncryptionImpl() }
    bindProvider<PasswordValidator> { PasswordValidatorImpl() }
}