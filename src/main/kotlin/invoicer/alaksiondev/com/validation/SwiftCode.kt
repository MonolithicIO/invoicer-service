package invoicer.alaksiondev.com.validation

fun validateSwiftCode(code: String): Boolean {
    val pattern = Regex("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?\$")
    return pattern.matches(code)
}