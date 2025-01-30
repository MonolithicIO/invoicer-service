package utils.password.impl

interface PasswordValidator {
    fun validate(password: String): PasswordStrength
}

sealed interface PasswordStrength {

    data object STRONG : PasswordStrength
    data class WEAK(val reason: String) : PasswordStrength
}

internal class PasswordValidatorImpl : PasswordValidator {

    override fun validate(password: String): PasswordStrength {
        val lengthValid = password.length >= 8
        val upperCaseValid = password.any { it.isUpperCase() }
        val lowerCaseValid = password.any { it.isLowerCase() }
        val digitValid = password.any { it.isDigit() }
        val specialCharValid = password.any { !it.isLetterOrDigit() }

        return when {
            !lengthValid -> PasswordStrength.WEAK("Password must be at least 8 characters long")
            !upperCaseValid -> PasswordStrength.WEAK("Password must contain at least one uppercase letter")
            !lowerCaseValid -> PasswordStrength.WEAK("Password must contain at least one lowercase letter")
            !digitValid -> PasswordStrength.WEAK("Password must contain at least one digit")
            !specialCharValid -> PasswordStrength.WEAK("Password must contain at least one special character")
            else -> PasswordStrength.STRONG
        }
    }

}