package io.github.monolithic.invoicer.consumers.messages.types

import io.github.monolithic.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class InvoicePdfMessage(
    @Serializable(with = JavaUUIDSerializer::class)
    val invoiceId: UUID,
    @Serializable(with = JavaUUIDSerializer::class)
    val userId: UUID,
    @Serializable(with = JavaUUIDSerializer::class)
    val companyId: UUID
) : Message
