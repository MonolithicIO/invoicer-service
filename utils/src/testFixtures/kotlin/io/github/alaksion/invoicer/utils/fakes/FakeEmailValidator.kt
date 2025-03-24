package io.github.alaksion.invoicer.utils.fakes

import io.github.alaksion.invoicer.utils.validation.EmailValidator

class FakeEmailValidator : EmailValidator {

    var response = true

    override fun validate(email: String): Boolean {
        return response
    }
}