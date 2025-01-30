package foundation.validator.impl

interface IbanValidator {
    fun validate(iban: String): Boolean
}

// TODO -> Check if this is correct
internal object IbanValidatorImpl : IbanValidator {
    override fun validate(iban: String): Boolean {
        return iban.trim().isNotBlank()
    }
}