package services.fakes

import io.github.alaksion.invoicer.foundation.validation.EmailValidator

class FakeEmailValidator : EmailValidator {

    var response = true

    override fun validate(email: String): Boolean {
        return response
    }
}