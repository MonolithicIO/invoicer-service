package foundation.cache.redis.jedis

import foundation.cache.redis.RedisInstance
import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import kotlinx.coroutines.*
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.params.SetParams
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal class JedisRedisManager(
    private val secrets: SecretsProvider,
    private val logger: Logger,
    dispatcher: CoroutineDispatcher
) : RedisInstance {

    private var healthJob: Job? = null
    private var pool: JedisPool? = createNewPool()

    init {
        healthJob = CoroutineScope(dispatcher).launch {
            while (true) {
                logger.log(
                    type = JedisRedisManager::class,
                    message = "Starting Redis Health check",
                    level = LogLevel.Debug
                )
                if (reconnectIfNeeded()) {
                    logger.log(
                        type = JedisRedisManager::class,
                        message = "Redis is healthy",
                        level = LogLevel.Debug
                    )
                } else {
                    logger.log(
                        type = JedisRedisManager::class,
                        message = "Redis is unhealthy. Launching reconnect process in $REDIS_HEALTH_CHECK_INTERVAL",
                        level = LogLevel.Error
                    )
                }
                delay(REDIS_HEALTH_CHECK_INTERVAL)
            }
        }
    }

    override fun setKey(key: String, value: String) {
        launchRedisQuery(queryType = "insert") { connection ->
            connection.set(
                key,
                value,
                SetParams().ex(secrets.getSecret(SecretKeys.REDIS_TTL).toLong())
            )
        }
    }

    override fun getKey(key: String): String? {
        return launchRedisQuery(queryType = "get") { connection -> connection.get(key) }
    }

    override fun clearKey(key: String) {
        launchRedisQuery(queryType = "delete") { connection ->
            connection.del(key)
        }
    }

    private fun <T> launchRedisQuery(
        queryType: String,
        block: (Jedis) -> T,
    ): T? {
        return runCatching {
            pool?.resource?.let { resource ->
                block(resource).also {
                    pool?.returnResource(resource)
                }
            } ?: run {
                logger.log(
                    type = this::class,
                    message = "Failed to launch Redis Query: $queryType. Pool not available",
                    level = LogLevel.Error
                )
                null
            }
        }.fold(
            onSuccess = { it },
            onFailure = {
                logger.log(
                    type = this::class,
                    message = "Failed to launch Redis Query: $queryType. Pool connection failed or timed out",
                    level = LogLevel.Error
                )
                null
            }
        )
    }

    private fun createNewPool(): JedisPool? {
        val poolConfig = JedisPoolConfig().apply {
            maxTotal = 10
            maxIdle = 5
            minIdle = 1
            testOnBorrow = true
            testWhileIdle = true
            setMaxWait(1.seconds.toJavaDuration())
        }

        val instance = runCatching {
            JedisPool(
                poolConfig,
                secrets.getSecret(SecretKeys.REDIS_HOST),
                secrets.getSecret(SecretKeys.REDIS_PORT).toInt(),
                2000,
                null, // password
                0 // database
            )
        }.fold(
            onSuccess = { it },
            onFailure = { error ->
                logger.log(
                    type = this::class,
                    message = "Failed to create new Jedis pool",
                    throwable = error,
                    level = LogLevel.Error
                )
                null
            }
        )

        return instance?.let {
            if (healthCheck(instance)) instance
            else null
        }
    }

    private fun reconnectIfNeeded(): Boolean {
        pool?.let { safePool ->
            if (healthCheck(safePool)) return true
            else {
                logger.log(
                    type = this::class,
                    message = "Health check failed. Closing existing connection",
                    level = LogLevel.Debug
                )
                disconnect()
            }
        }

        logger.log(
            type = this::class,
            message = "Redis connection lost. Reconnecting...",
            level = LogLevel.Warn
        )

        return reconnect().also { hasConnection ->
            if (hasConnection) {
                logger.log(
                    type = this::class,
                    message = "Reconnected to Redis successfully",
                    level = LogLevel.Debug
                )
            } else {
                logger.log(
                    type = this::class,
                    message = "Failed to reconnect to Redis",
                    level = LogLevel.Error
                )
            }
        }
    }

    private fun healthCheck(pool: JedisPool): Boolean {
        return runCatching {
            pool.resource.use { jedis ->
                jedis?.ping() == "PONG"
            }
        }.fold(
            onSuccess = { true },
            onFailure = { false }
        )
    }

    private fun reconnect(): Boolean {
        pool?.close()
        return createNewPool()?.let { safePool ->
            if (healthCheck(safePool)) {
                pool = safePool
                true
            } else {
                false
            }
        } ?: false
    }

    private fun disconnect() {
        pool?.close()
        pool = null
    }

    companion object {
        val REDIS_HEALTH_CHECK_INTERVAL = 5.seconds
    }
}