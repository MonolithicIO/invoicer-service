package foundation.env.fakes

import foundation.env.InvoicerEnvironment

class FakeInvoicerEnvironment : InvoicerEnvironment {

    var callStack = mutableListOf<String>()
    var response: String? = null

    override fun getVariable(key: String): String? {
        callStack.add(key)
        return response
    }
}