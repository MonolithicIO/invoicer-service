package io.github.monolithic.invoicer.utils.validation

interface EmailValidator {
    fun validate(email: String): Boolean
}

internal object EmailValidatorImpl : EmailValidator {
    override fun validate(email: String): Boolean {
        return email.matches(emailRegex.toRegex())
    }

    private const val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
}
