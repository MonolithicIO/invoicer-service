package foundation.redis.impl.di

import foundation.redis.impl.RedisInstance
import foundation.redis.impl.jedis.JedisRedisManager
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val redisDiModule = DI.Module("redisDiModule") {
    bindSingleton<RedisInstance> { JedisRedisManager(secrets = instance()) }
}