package io.github.monolithic.invoicer.foundation.password.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import io.github.monolithic.invoicer.foundation.password.PasswordEncryption
import io.github.monolithic.invoicer.foundation.password.PasswordEncryptionImpl
import io.github.monolithic.invoicer.foundation.password.PasswordValidator
import io.github.monolithic.invoicer.foundation.password.PasswordValidatorImpl

val utilPasswordDi = DI.Module("util-password") {
    bindProvider<PasswordEncryption> { PasswordEncryptionImpl() }
    bindProvider<PasswordValidator> { PasswordValidatorImpl() }
}
