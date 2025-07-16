package io.github.monolithic.invoicer.foundation.secrets.fakes

import io.github.monolithic.invoicer.foundation.env.application.InvoicerEnvironment

class FakeInvoicerEnvironment : InvoicerEnvironment {

    var callStack = mutableListOf<String>()
    var response: String? = null

    override fun getVariable(key: String): String? {
        callStack.add(key)
        return response
    }
}