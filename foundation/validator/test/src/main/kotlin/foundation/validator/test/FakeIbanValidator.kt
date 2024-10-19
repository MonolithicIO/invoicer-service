package foundation.validator.test

import foundation.validator.api.IbanValidator

class FakeIbanValidator : IbanValidator {

    var response = true

    override fun validate(iban: String): Boolean {
        return response
    }
}