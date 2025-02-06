package foundation.cache.impl.di

import foundation.cache.impl.CacheHandler
import foundation.cache.impl.redis.RedisCacheHandler
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val cacheDiModule = DI.Module("cacheDiModule") {
    bindProvider<CacheHandler> {
        RedisCacheHandler(
            redisInstance = instance()
        )
    }
}