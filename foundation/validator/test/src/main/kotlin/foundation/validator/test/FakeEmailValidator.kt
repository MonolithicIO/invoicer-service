package foundation.validator.test

import foundation.validator.api.EmailValidator

class FakeEmailValidator : EmailValidator {

    var response = true

    override fun validate(email: String): Boolean {
        return response
    }
}