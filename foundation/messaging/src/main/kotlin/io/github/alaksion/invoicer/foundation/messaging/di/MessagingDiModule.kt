package io.github.alaksion.invoicer.foundation.messaging.di

import io.github.alaksion.invoicer.foundation.messaging.MessageConsumer
import io.github.alaksion.invoicer.foundation.messaging.MessageProducer
import io.github.alaksion.invoicer.foundation.messaging.kafka.KafkaConsumer
import io.github.alaksion.invoicer.foundation.messaging.kafka.KafkaProducer
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val messagingDiModule = DI.Module("messaging-di") {
    bindSingleton<MessageProducer> {
        KafkaProducer(secrets = instance())
    }

    bindSingleton<MessageConsumer> {
        KafkaConsumer(secrets = instance())
    }
}