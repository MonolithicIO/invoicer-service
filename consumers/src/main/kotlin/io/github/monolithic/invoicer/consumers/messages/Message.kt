package io.github.monolithic.invoicer.consumers.messages

import io.github.monolithic.invoicer.consumers.messages.types.InvoicePdfMessage
import io.github.monolithic.invoicer.consumers.messages.types.SendEmailMessage
import io.github.monolithic.invoicer.consumers.messages.types.Unkown
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = MessageSerializer::class)
internal interface Message

object MessageKeys {
    const val RESET_PASSWORD = "send_reset_password_email"
    const val INVOICE_PDF_GENERATE = "invoice_generate_pdf"
}

private object MessageSerializer : JsonContentPolymorphicSerializer<Message>(Message::class) {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
        serialName = "io.github.monolithic.invoicer.consumers.messages.Message"
    )

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Message> {
        val type = element.jsonObject["type"]?.jsonPrimitive?.content

        return when (type) {
            MessageKeys.INVOICE_PDF_GENERATE -> InvoicePdfMessage.serializer()
            MessageKeys.RESET_PASSWORD -> SendEmailMessage.serializer()
            else -> Unkown.serializer()
        }
    }
}
