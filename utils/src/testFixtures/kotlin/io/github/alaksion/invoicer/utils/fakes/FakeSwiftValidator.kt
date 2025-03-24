package io.github.alaksion.invoicer.utils.fakes

import io.github.alaksion.invoicer.utils.validation.SwiftValidator

class FakeSwiftValidator : SwiftValidator {

    var response = true

    override fun validate(swift: String): Boolean {
        return response
    }
}