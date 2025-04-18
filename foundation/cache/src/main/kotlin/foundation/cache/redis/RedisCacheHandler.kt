package foundation.cache.redis

import foundation.cache.CacheHandler
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

internal class RedisCacheHandler(
    private val redisInstance: RedisInstance,
    private val logger: Logger
) : CacheHandler {

    override suspend fun <T> set(
        key: String,
        value: T,
        serializer: KSerializer<T>,
        ttlSeconds: Long,
    ) {
        runCatching {
            val encoded = Json.encodeToString(
                serializer = serializer,
                value = value
            )

            redisInstance.setKey(
                key = key,
                value = encoded,
                ttlSeconds = ttlSeconds
            )
        }.onFailure {
            logger.log(
                type = RedisCacheHandler::class,
                message = it.message ?: "Failed to encode data with ${serializer::class.simpleName}",
                throwable = it,
                level = LogLevel.Error
            )
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
                    logger.log(
                        type = RedisCacheHandler::class,
                        message = it.message ?: "Failed to decode data with ${serializer::class.simpleName}",
                        throwable = it,
                        level = LogLevel.Error
                    )
                    null
                }
            )
        }
    }

    override suspend fun delete(key: String) = redisInstance.clearKey(key)
}