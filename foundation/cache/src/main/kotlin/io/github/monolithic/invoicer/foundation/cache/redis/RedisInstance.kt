package io.github.monolithic.invoicer.foundation.cache.redis

interface RedisInstance {
    fun setKey(key: String, value: String, ttlSeconds: Long)
    fun getKey(key: String): String?
    fun clearKey(key: String)
}
