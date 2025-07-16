package io.github.monolithic.invoicer.foundation.messaging.kafka

import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.messaging.MessageConsumer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

internal class KafkaConsumer(
    private val secrets: SecretsProvider,
    private val coroutineScope: CoroutineScope,
    private val logger: Logger,
) : MessageConsumer {

    private val _messageStream = MutableSharedFlow<String>()
    override val messageStream: SharedFlow<String> = _messageStream

    private var messagingJob: Job? = null

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

    override suspend fun startConsuming() {
        messagingJob = coroutineScope.launch {
            while (true) {
                val records = consumer.poll(1.seconds.toJavaDuration())
                for (record in records) {
                    _messageStream.emit(record.value())
                    logger.log(
                        type = KafkaConsumer::class,
                        message = "Received message: ${record.value()} from topic: ${record.topic()}",
                        level = LogLevel.Debug,
                    )
                }
                consumer.commitSync()
            }
        }
    }

    override fun close() {
        messagingJob?.cancel()
        consumer.close()
    }
}
