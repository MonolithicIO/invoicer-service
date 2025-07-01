package io.github.alaksion.invoicer.foundation.messaging.kafka

import io.github.alaksion.invoicer.foundation.env.secrets.SecretKeys
import io.github.alaksion.invoicer.foundation.env.secrets.SecretsProvider
import io.github.alaksion.invoicer.foundation.messaging.MessageProducer
import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import org.apache.kafka.clients.producer.KafkaProducer
import java.util.*

internal class KafkaProducer(
    private val secrets: SecretsProvider
) : MessageProducer {

    private val producer by lazy {
        val properties = Properties()
        properties["bootstrap.servers"] = secrets.getSecret(SecretKeys.KAFKA_BOOTSTRAP)
        properties["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        properties["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
        KafkaProducer<String, String>(properties)
    }

    override suspend fun produceMessage(topic: MessageTopic, key: String, value: String) {
//        val message = ProducerRecord(topic.topicId, key, value)
//        producer.send(message) { metadata, exception ->
//            if (exception != null) {
//                println("Failed to send message: ${exception.message}")
//            } else {
//                println("Message succeeded with metadata: $metadata")
//            }
//        }
    }
}