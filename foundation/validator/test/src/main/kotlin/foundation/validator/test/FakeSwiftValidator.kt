package foundation.validator.test

import foundation.validator.impl.SwiftValidator

class FakeSwiftValidator : SwiftValidator {

    var response = true

    override fun validate(swift: String): Boolean {
        return response
    }
}