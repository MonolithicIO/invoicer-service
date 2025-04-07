package io.github.alaksion.invoicer.consumers.messages.types

import kotlinx.serialization.Serializable


@Serializable
internal data class GeneratePdfMessage(
    val invoiceId: String,
    val userId: String
) : Message
