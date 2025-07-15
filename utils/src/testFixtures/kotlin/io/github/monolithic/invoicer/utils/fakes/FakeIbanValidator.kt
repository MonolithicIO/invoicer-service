package io.github.monolithic.invoicer.utils.fakes

import io.github.monolithic.invoicer.utils.validation.IbanValidator

class FakeIbanValidator : IbanValidator {

    var response = true

    override fun validate(iban: String): Boolean {
        return response
    }
}