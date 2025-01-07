package foundation.env

import io.ktor.server.application.*

interface Environment {
    fun getVariable(key: String): String?
}

internal class EnvironmentImpl(
    private val application: Application
) : Environment {

    override fun getVariable(key: String): String? {
        return application.environment.config.propertyOrNull(key)?.getString()
    }
}