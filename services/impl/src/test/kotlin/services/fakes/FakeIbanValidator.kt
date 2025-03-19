package services.fakes

import io.github.alaksion.invoicer.utils.validation.IbanValidator

class FakeIbanValidator : IbanValidator {

    var response = true

    override fun validate(iban: String): Boolean {
        return response
    }
}