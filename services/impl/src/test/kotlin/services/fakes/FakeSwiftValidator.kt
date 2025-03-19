package services.fakes

import io.github.alaksion.invoicer.foundation.validation.SwiftValidator

class FakeSwiftValidator : SwiftValidator {

    var response = true

    override fun validate(swift: String): Boolean {
        return response
    }
}