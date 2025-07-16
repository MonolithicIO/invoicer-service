package io.github.monolithic.invoicer.foundation.cache.fakes

import io.github.monolithic.invoicer.foundation.cache.redis.RedisInstance

class FakeRedisInstance : RedisInstance {
    var getKeyResponse: String? = null

    val setCallStack = mutableListOf<Pair<String, String>>()
    val getCallStack = mutableListOf<String>()
    val clearCallStack = mutableListOf<String>()

    override fun setKey(key: String, value: String, ttlSeconds: Long) {
        setCallStack.add(Pair(key, value))
    }

    override fun getKey(key: String): String? {
        getCallStack.add((key))
        return getKeyResponse
    }

    override fun clearKey(key: String) {
        clearCallStack.add(key)
    }
}
