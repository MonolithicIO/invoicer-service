package io.github.alaksion.invoicer.server.util

private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"

fun isValidEmail(email: String): Boolean {
    return email.matches(emailRegex.toRegex())
}