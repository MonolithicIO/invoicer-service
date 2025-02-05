package foundation.cache.impl.lettuce

import foundation.cache.impl.CacheHandler
import foundation.impl.SecretKeys
import foundation.impl.SecretsProvider
import io.lettuce.core.RedisClient
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class LettuceCacheHandlerImpl(
    private val secretsProvider: SecretsProvider
) : CacheHandler {

    private val redis by lazy {
        RedisClient.create(
            secretsProvider.getSecret(SecretKeys.REDIS_URL)
        ).connect()
    }

    override suspend fun <T> set(
        key: String,
        value: T,
        serializer: KSerializer<T>
    ) {
        suspendCoroutine { continuation ->
            val parsed = parseToString(value, serializer)

            if (parsed == null) {
                continuation.resume(Unit)
                return@suspendCoroutine
            }

            val future = redis.async().set(key, parsed)

            future.handle { _, error ->
                if (error != null) {
                    // Log Error
                    continuation.resume(Unit)
                }
                continuation.resume(Unit)
            }
        }
    }

    override suspend fun <T> get(
        key: String,
        serializer: KSerializer<T>
    ): T? {
        return suspendCoroutine { continuation ->
            val future = redis.async().get(key)

            future.handle { cachedString, error ->
                if (error != null) {
                    // log error
                    continuation.resume(null)
                } else {
                    val parsed = parseFromString(cachedString, serializer)

                    if (parsed == null) {
                        continuation.resume(null)
                    } else {
                        continuation.resume(parsed)
                    }
                }
            }
        }
    }


    private fun <T> parseToString(value: T, serializer: KSerializer<T>): String? {
        return runCatching {
            Json.encodeToString(serializer = serializer, value = value)
        }.fold(
            onSuccess = {
                it
            },
            onFailure = {
                // Log serialization error
                null
            }
        )
    }

    private fun <T> parseFromString(value: String, serializer: KSerializer<T>): T? {
        return runCatching {
            Json.decodeFromString(deserializer = serializer, string = value)
        }.fold(
            onSuccess = {
                it
            },
            onFailure = {
                // Log serialization error
                null
            }
        )
    }
}
