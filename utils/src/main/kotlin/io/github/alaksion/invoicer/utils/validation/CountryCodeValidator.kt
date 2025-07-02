package io.github.alaksion.invoicer.utils.validation

import java.util.*

interface CountryCodeValidator {
    fun validate(countryCode: String): Boolean
}

internal class CountryCodeValidatorImpl : CountryCodeValidator {

    private val validCountryCodes by lazy {
        Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3)
    }

    override fun validate(countryCode: String): Boolean {
        return validCountryCodes.contains(countryCode.uppercase())
    }
}
