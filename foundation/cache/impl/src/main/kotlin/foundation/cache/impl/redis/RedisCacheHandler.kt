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
        runCatching {
            val encoded = Json.encodeToString(
                serializer = serializer,
                value = value
            )

            redisInstance.setKey(
                key = key,
                value = encoded
            )
        }.onFailure {
            // Log error
        }
    }

    override suspend fun <T> get(
        key: String,
        serializer: KSerializer<T>
    ): T? {
        return redisInstance.getKey(key)?.let { cachedItem ->
            runCatching {
                Json.decodeFromString(
                    deserializer = serializer,
                    string = cachedItem
                )
            }.fold(
                onSuccess = { it },
                onFailure = {
                    // Log error
                    null
                }
            )
        }
    }

    override suspend fun delete(key: String) = redisInstance.clearKey(key)
}