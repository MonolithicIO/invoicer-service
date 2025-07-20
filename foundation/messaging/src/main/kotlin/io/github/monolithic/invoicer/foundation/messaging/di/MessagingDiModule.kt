package io.github.monolithic.invoicer.foundation.messaging.di

import io.github.monolithic.invoicer.foundation.messaging.MessageConsumer
import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.local.LocalMessageHandler
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val messagingDiModule = DI.Module("messaging-di") {
    bindSingleton { LocalMessageHandler() }
    bindProvider<MessageConsumer> { instance<LocalMessageHandler>() }
    bindProvider<MessageProducer> { instance<LocalMessageHandler>() }
}
