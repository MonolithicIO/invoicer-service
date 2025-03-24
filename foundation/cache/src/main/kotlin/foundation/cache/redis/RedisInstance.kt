package foundation.cache.redis

interface RedisInstance {
    fun setKey(key: String, value: String)
    fun getKey(key: String): String?
    fun clearKey(key: String)
}