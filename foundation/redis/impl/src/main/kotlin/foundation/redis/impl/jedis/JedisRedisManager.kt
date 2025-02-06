package foundation.redis.impl.jedis

import foundation.impl.SecretKeys
import foundation.impl.SecretsProvider
import foundation.redis.impl.RedisInstance
import redis.clients.jedis.JedisPool

internal class JedisRedisManager(
    private val secrets: SecretsProvider
) : RedisInstance {

    private val redisPool by lazy {
        JedisPool(
            secrets.getSecret(SecretKeys.REDIS_HOST),
            secrets.getSecret(SecretKeys.REDIS_PORT).toInt()
        )
    }

    override fun setKey(key: String, value: String) {
        val resource = redisPool.resource
        resource.set(key, value)
    }

    override fun getKey(key: String): String? {
        val resource = redisPool.resource
        return resource.get(key)
    }
}