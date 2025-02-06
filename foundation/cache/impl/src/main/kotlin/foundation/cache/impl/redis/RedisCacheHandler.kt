package foundation.cache.impl.redis

import foundation.cache.impl.CacheHandler
import foundation.redis.impl.RedisInstance
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal class RedisCacheHandler(
    private val redisInstance: RedisInstance
) : CacheHandler {

    override suspend fun <T> set(
        key: String,
        value: T,
        serializer: KSerializer<T>
    ) {
        val encoded = Json.encodeToString(
            serializer = serializer,
            value = value
        )

        redisInstance.setKey(
            key = key,
            value = encoded
        )
    }

    override suspend fun <T> get(
        key: String,
        serializer: KSerializer<T>
    ): T? {
        return redisInstance.getKey(key)?.let { cachedItem ->
            Json.decodeFromString(
                deserializer = serializer,
                string = cachedItem
            )
        }
    }
}