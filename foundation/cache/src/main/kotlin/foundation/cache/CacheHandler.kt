package foundation.cache

import kotlinx.serialization.KSerializer

interface CacheHandler {
    /***
     * Store the value in the cache with the given key and serializer.
     * Also defines a TTL for the cache entry
     *
     * @param key The key to store the value under
     * @param value The value to store
     * @param serializer The serializer to use for encoding [value] into a String.
     * @param ttlSeconds The time to live for the cache entry in seconds. Default is 60 seconds.
     * */
    suspend fun <T> set(
        key: String,
        value: T,
        serializer: KSerializer<T>,
        ttlSeconds: Long = DEFAULT_TTL_SECONDS
    )

    suspend fun <T> get(
        key: String,
        serializer: KSerializer<T>
    ): T?

    suspend fun delete(key: String)

    companion object {
        const val DEFAULT_TTL_SECONDS = 60L
    }
}