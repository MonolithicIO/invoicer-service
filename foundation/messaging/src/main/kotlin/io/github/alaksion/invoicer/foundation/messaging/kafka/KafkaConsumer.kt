package io.github.alaksion.invoicer.foundation.messaging.kafka

import foundation.secrets.SecretKeys
import foundation.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.messaging.MessageConsumer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

internal class KafkaConsumer(
    private val secrets: SecretsProvider
) : MessageConsumer {

    private val consumer by lazy {
        val properties = Properties()
        properties["bootstrap.servers"] = secrets.getSecret(SecretKeys.KAFKA_BOOTSTRAP)
        properties["key.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        properties["value.deserializer"] = "org.apache.kafka.common.serialization.StringDeserializer"
        properties["group.id"] = "invoicer-service-group"
        properties["enable.auto.commit"] = "false"
        KafkaConsumer<String, String>(properties).apply {
            subscribe(MessageTopic.entries.map { it.topicId })
        }
    }

    override suspend fun consumeMessages(): Flow<String> {
        return flow {
            while (currentCoroutineContext().isActive) {
                val records = consumer.poll(100.milliseconds.toJavaDuration())
                for (record in records) {
                    println("Received message: ${record.value()} from topic: ${record.topic()}")
                    emit(record.value())
                    consumer.commitSync()
                }
            }
        }
    }

    override fun close() {
        consumer.close()
    }
}