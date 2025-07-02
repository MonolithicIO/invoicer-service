package io.github.alaksion.invoicer.utils.serialization

import io.github.alaksion.invoicer.utils.uuid.parseUuid
import java.util.UUID
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class JavaUUIDSerializer : KSerializer<UUID> {

    override val descriptor = buildClassSerialDescriptor("UUID")

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return parseUuid(decoder.decodeString())
    }
}
