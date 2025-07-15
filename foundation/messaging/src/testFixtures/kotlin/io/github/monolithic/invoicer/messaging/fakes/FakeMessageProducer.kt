package io.github.monolithic.invoicer.messaging.fakes

import io.github.monolithic.invoicer.foundation.messaging.MessageProducer
import io.github.monolithic.invoicer.foundation.messaging.MessageTopic

class FakeMessageProducer : MessageProducer {

    var calls = 0
        private set

    val messages = mutableListOf<Triple<MessageTopic, String, String>>()

    override suspend fun produceMessage(topic: MessageTopic, key: String, value: String) {
        calls++
        messages.add(Triple(topic, key, value))
    }
}