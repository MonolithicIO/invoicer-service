package io.github.monolithic.invoicer.utils.fakes

import io.github.monolithic.invoicer.utils.validation.SwiftValidator

class FakeSwiftValidator : SwiftValidator {

    var response = true

    override fun validate(swift: String): Boolean {
        return response
    }
}