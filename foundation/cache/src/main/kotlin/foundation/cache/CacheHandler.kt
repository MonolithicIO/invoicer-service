package foundation.cache

import kotlinx.serialization.KSerializer

interface CacheHandler {
    suspend fun <T> set(
        key: String,
        value: T,
        serializer: KSerializer<T>
    )

    suspend fun <T> get(
        key: String,
        serializer: KSerializer<T>
    ): T?

    suspend fun delete(key: String)
}