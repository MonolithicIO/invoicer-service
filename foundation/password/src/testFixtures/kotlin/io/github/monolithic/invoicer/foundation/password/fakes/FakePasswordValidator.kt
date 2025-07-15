package io.github.monolithic.invoicer.foundation.password.fakes

import io.github.monolithic.invoicer.foundation.password.PasswordStrength
import io.github.monolithic.invoicer.foundation.password.PasswordValidator

class FakePasswordValidator : PasswordValidator {

    var response: () -> PasswordStrength = { PasswordStrength.STRONG }

    override fun validate(password: String): PasswordStrength {
        return response()
    }
}