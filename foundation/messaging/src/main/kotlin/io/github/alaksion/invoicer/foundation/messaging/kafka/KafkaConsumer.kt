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
        properties["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        properties["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        properties["group.id"] = "invoicer-service-group"
        properties["enable.auto.commit"] = "false"
        KafkaConsumer<String, String>(properties).apply {
            subscribe(MessageTopic.entries.map { it.topicId })
        }
    }

    override suspend fun consumeMessages(): Flow<Unit> {
        return flow {
            while (currentCoroutineContext().isActive) {
                val records = consumer.poll(100.milliseconds.toJavaDuration())
                for (record in records) {
                    // Message processor Logic
                    // If success then commit the offset
                    // If failure then do not commit the offset
                    println("Received message: ${record.value()} from topic: ${record.topic()}")
                    consumer.commitSync()
                }
            }
        }
    }
}