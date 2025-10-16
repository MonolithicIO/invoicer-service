package io.github.monolithic.invoicer.utils.fakes

import io.github.monolithic.invoicer.utils.code.SixDigitGenerator

class FakeSixDigitGenerator : SixDigitGenerator {

    var response = "123456"

    override fun generateSixDigitCode(): String {
        return response
    }
}