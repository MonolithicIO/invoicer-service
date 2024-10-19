package foundation.validator.test

import foundation.validator.api.SwiftValidator

class FakeSwiftValidator : SwiftValidator {

    var response = true

    override fun validate(swift: String): Boolean {
        return response
    }
}