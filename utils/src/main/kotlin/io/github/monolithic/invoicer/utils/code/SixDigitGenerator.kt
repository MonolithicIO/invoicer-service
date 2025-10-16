package io.github.monolithic.invoicer.utils.code

interface SixDigitGenerator {
    fun generateSixDigitCode(): String
}

internal object SixDigitGeneratorImpl : SixDigitGenerator {
    override fun generateSixDigitCode(): String {
        return CODE_RANGE.random().toString().padStart(CODE_MIN_LENGTH, PADDING_CHAR)
    }

    @Suppress("MagicNumber")
    val CODE_RANGE = (0..999999)
    const val CODE_MIN_LENGTH = 6
    const val PADDING_CHAR = '0'
}
