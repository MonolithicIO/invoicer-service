package io.github.monolithic.invoicer.foundation.cache.redis

import io.github.monolithic.invoicer.foundation.cache.fakes.FakeRedisInstance
import io.github.monolithic.invoicer.foundation.log.fakes.FakeLogger
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RedisCacheHandlerTest {

    private lateinit var handler: RedisCacheHandler
    private lateinit var redisInstance: FakeRedisInstance
    private lateinit var logger: FakeLogger


    @BeforeTest
    fun setUp() {
        redisInstance = FakeRedisInstance()
        logger = FakeLogger()
        handler = RedisCacheHandler(
            redisInstance = redisInstance,
            logger = logger
        )
    }

    @Test
    fun `should set given key with serialized value`() = runTest {
        handler.set(
            key = "key",
            value = Sample("hello"),
            serializer = Sample.serializer()
        )

        assertEquals(
            expected = 1,
            actual = redisInstance.setCallStack.size
        )

        assertEquals(
            expected = "key",
            actual = redisInstance.setCallStack.first().first
        )

        assertEquals(
            expected = "{\"id\":\"hello\"}",
            actual = redisInstance.setCallStack.first().second
        )
    }

    @Test
    fun `should call logger if set key fails to serialize`() = runTest {
        handler.set(
            key = "key",
            value = Sample("hello"),
            serializer = FakeSerializer()
        )

        assertEquals(
            expected = 1,
            actual = logger.logTypeCalls
        )
    }

    @Test
    fun `should get given key with deserialized value`() = runTest {
        redisInstance.getKeyResponse = "{\"id\":\"hello\"}"

        val result = handler.get(
            key = "key",
            serializer = Sample.serializer()
        )

        assertEquals(
            expected = Sample("hello"),
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = redisInstance.getCallStack.size
        )

        assertEquals(
            expected = "key",
            actual = redisInstance.getCallStack.first()
        )
    }

    @Test
    fun `should get given key with null value`() = runTest {
        redisInstance.getKeyResponse = null

        val result = handler.get(
            key = "key",
            serializer = Sample.serializer()
        )

        assertEquals(
            expected = null,
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = redisInstance.getCallStack.size
        )

        assertEquals(
            expected = "key",
            actual = redisInstance.getCallStack.first()
        )
    }

    @Test
    fun `should call logger if get key fails to serialize`() = runTest {
        redisInstance.getKeyResponse = "{\"na\":\"hello\"}"

        handler.get(
            key = "key",
            serializer = FakeSerializer()
        )

        assertEquals(
            expected = 1,
            actual = logger.logTypeCalls
        )
    }

    @Test
    fun `should delete given key`() = runTest {
        handler.delete("key")

        assertEquals(
            expected = 1,
            actual = redisInstance.clearCallStack.size
        )

        assertEquals(
            expected = "key",
            actual = redisInstance.clearCallStack.first()
        )
    }
}

@Serializable
private data class Sample(
    val id: String,
)

private class FakeSerializer : KSerializer<Sample> {
    override val descriptor: SerialDescriptor = serialDescriptor<Sample>()

    override fun deserialize(decoder: Decoder): Sample {
        throw IllegalArgumentException("")
    }

    override fun serialize(encoder: Encoder, value: Sample) {
        throw IllegalArgumentException("")
    }
}
