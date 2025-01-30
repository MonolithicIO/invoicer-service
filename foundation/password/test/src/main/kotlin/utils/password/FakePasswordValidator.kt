package utils.password

import utils.password.impl.PasswordStrength
import utils.password.impl.PasswordValidator

class FakePasswordValidator : PasswordValidator {

    var response: () -> PasswordStrength = { PasswordStrength.STRONG }

    override fun validate(password: String): PasswordStrength {
        return response()
    }
}