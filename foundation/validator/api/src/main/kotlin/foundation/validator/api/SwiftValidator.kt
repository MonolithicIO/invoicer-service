package foundation.validator.api

interface SwiftValidator {
    fun validate(swift: String): Boolean
}

internal object SwiftValidatorImpl : SwiftValidator {
    override fun validate(swift: String): Boolean {
        val pattern = Regex("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?\$")
        return pattern.matches(swift)
    }
}