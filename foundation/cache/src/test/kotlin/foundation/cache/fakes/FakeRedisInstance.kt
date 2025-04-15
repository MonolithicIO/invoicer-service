package foundation.cache.fakes

import foundation.cache.redis.RedisInstance

class FakeRedisInstance : RedisInstance {
    var getKeyResponse: String? = null

    val setCallStack = mutableListOf<Pair<String, String>>()
    val getCallStack = mutableListOf<String>()
    val clearCallStack = mutableListOf<String>()

    override fun setKey(key: String, value: String) {
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