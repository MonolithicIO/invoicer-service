package io.github.monolithic.invoicer.consumers.messages.types

import io.github.monolithic.invoicer.consumers.messages.Message
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SendEmailMessage : Message {

    @Serializable
    data class ResetPasswordMessage(
        val token: String
    ) : SendEmailMessage
}
