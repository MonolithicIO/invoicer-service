package utils.password

class FakePasswordValidator : PasswordValidator {

    var response: () -> PasswordStrength = { PasswordStrength.STRONG }

    override fun validate(password: String): PasswordStrength {
        return response()
    }
}