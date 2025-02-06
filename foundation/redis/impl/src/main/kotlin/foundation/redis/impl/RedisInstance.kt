package foundation.redis.impl

interface RedisInstance {
    fun setKey(key: String, value: String)
    fun getKey(key: String): String?
}