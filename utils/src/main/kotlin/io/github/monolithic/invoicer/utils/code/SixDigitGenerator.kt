package io.github.monolithic.invoicer.utils.code

interface SixDigitGenerator {
    fun generateSixDigitCode(): String
}

internal object SixDigitGeneratorImpl : SixDigitGenerator {
    override fun generateSixDigitCode(): String {
        return (0..999999).random().toString().padStart(6, '0')
    }
}