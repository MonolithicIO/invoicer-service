package io.github.monolithic.invoicer.consumers.messages

import io.github.monolithic.invoicer.consumers.messages.types.GeneratePdfMessage
import io.github.monolithic.invoicer.consumers.messages.types.Message
import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.consumers.messages.types.Unkown
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object MessageSerializer : JsonContentPolymorphicSerializer<Message>(Message::class) {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        serialName = "io.github.alaksion.invoicer.consumers.messages.types.Message"
    )

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Message> {
        val type = element.jsonObject["type"]?.jsonPrimitive?.content

        return when (type) {
            MessageType.GENERATE_PDF.messageId -> GeneratePdfMessage.serializer()
            MessageType.SEND_PDF_EMAIL.messageId -> SendEmailMessage.serializer()
            else -> Unkown.serializer()
        }
    }
}
