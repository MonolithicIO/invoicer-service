package io.github.alaksion.invoicer.foundation.validation

interface EmailValidator {
    fun validate(email: String): Boolean
}

internal object EmailValidatorImpl : EmailValidator {
    override fun validate(email: String): Boolean {
        return email.matches(emailRegex.toRegex())
    }

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
}
