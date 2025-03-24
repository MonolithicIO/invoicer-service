package foundation.password.fakes

import foundation.password.PasswordStrength
import foundation.password.PasswordValidator

class FakePasswordValidator : PasswordValidator {

    var response: () -> PasswordStrength = { PasswordStrength.STRONG }

    override fun validate(password: String): PasswordStrength {
        return response()
    }
}