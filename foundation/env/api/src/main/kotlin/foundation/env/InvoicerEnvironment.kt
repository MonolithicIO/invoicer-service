package foundation.env

import io.ktor.server.application.*

interface InvoicerEnvironment {
    fun getVariable(key: String): String?
}

internal class InvoicerEnvironmentImpl(
    private val application: Application
) : InvoicerEnvironment {

    override fun getVariable(key: String): String? {
        return application.environment.config.propertyOrNull(key)?.getString()
    }
}