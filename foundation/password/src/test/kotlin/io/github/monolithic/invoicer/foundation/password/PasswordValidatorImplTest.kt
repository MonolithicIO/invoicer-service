package io.github.monolithic.invoicer.foundation.password

import kotlin.test.Test
import kotlin.test.assertEquals

class PasswordValidatorImplTest {

    private val validator = PasswordValidatorImpl()

    @Test
    fun `returns WEAK when password is less than 8 characters`() {
        val result = validator.validate("Short1!")
        assertEquals(
            PasswordStrength.WEAK("Password must be at least 8 characters long"),
            result
        )
    }

    @Test
    fun `returns WEAK when password does not contain an uppercase letter`() {
        val result = validator.validate("lowercase1!")
        assertEquals(
            PasswordStrength.WEAK("Password must contain at least one uppercase letter"),
            result
        )
    }

    @Test
    fun `returns WEAK when password does not contain a lowercase letter`() {
        val result = validator.validate("UPPERCASE1!")
        assertEquals(
            PasswordStrength.WEAK("Password must contain at least one lowercase letter"),
            result
        )
    }

    @Test
    fun `returns WEAK when password does not contain a digit`() {
        val result = validator.validate("NoDigits!")
        assertEquals(
            PasswordStrength.WEAK("Password must contain at least one digit"),
            result
        )
    }

    @Test
    fun `returns WEAK when password does not contain a special character`() {
        val result = validator.validate("NoSpecial1")
        assertEquals(
            PasswordStrength.WEAK("Password must contain at least one special character"),
            result
        )
    }

    @Test
    fun `returns STRONG when password meets all criteria`() {
        val result = validator.validate("Strong1!")
        assertEquals(PasswordStrength.STRONG, result)
    }
}
