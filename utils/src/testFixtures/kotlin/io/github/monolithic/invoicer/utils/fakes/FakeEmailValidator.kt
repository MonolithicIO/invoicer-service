package io.github.monolithic.invoicer.utils.fakes

import io.github.monolithic.invoicer.utils.validation.EmailValidator

class FakeEmailValidator : EmailValidator {

    var response = true

    override fun validate(email: String): Boolean {
        return response
    }
}