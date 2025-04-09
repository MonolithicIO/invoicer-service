package io.github.alaksion.invoicer.consumers.messages.types

import io.github.alaksion.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
internal data class GeneratePdfMessage(
    @Serializable(with = JavaUUIDSerializer::class)
    val invoiceId: UUID,
    @Serializable(with = JavaUUIDSerializer::class)
    val userId: UUID
) : Message
