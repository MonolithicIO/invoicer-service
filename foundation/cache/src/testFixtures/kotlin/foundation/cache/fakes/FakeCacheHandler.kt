package foundation.cache.fakes

import foundation.cache.CacheHandler
import kotlinx.serialization.KSerializer

class FakeCacheHandler : CacheHandler {

    var getCacheResponse: Any? = null

    var getCacheCalls = 0
        private set

    val deleteCallStack = mutableListOf<String>()
    val setCallStack = mutableListOf<Pair<String, Any>>()

    override suspend fun <T> set(key: String, value: T, serializer: KSerializer<T>, ttlSeconds: Long) {
        setCallStack.add(Pair(key, value as Any))
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> get(key: String, serializer: KSerializer<T>): T? {
        getCacheCalls++
        return getCacheResponse as T
    }

    override suspend fun delete(key: String) {
        deleteCallStack.add(key)
    }
}