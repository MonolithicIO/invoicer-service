package io.github.monolithic.invoicer.consumers.messages.types

import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SendEmailMessage : Message {

    @Serializable
    data class ResetPasswordMessage(
        val token: String
    ) : SendEmailMessage
}
