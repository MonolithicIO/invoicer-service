package io.github.monolithic.invoicer.utils.uuid

interface UuidProvider {
    fun generateUuid(): String
}

internal object UuidProviderImpl : UuidProvider {
    override fun generateUuid(): String {
        return java.util.UUID.randomUUID().toString()
    }
}
