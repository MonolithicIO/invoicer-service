package foundation.cache.impl.di

import foundation.cache.impl.CacheHandler
import foundation.cache.impl.redis.RedisCacheHandler
import foundation.cache.impl.redis.RedisInstance
import foundation.cache.impl.redis.jedis.JedisRedisManager
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val cacheDiModule = DI.Module("cacheDiModule") {
    bindProvider<CacheHandler> {
        RedisCacheHandler(
            redisInstance = instance()
        )
    }

    bindSingleton<RedisInstance> { JedisRedisManager(secrets = instance()) }
}