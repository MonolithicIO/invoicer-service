package io.github.monolithic.invoicer.consumers.messages.types

import io.github.monolithic.invoicer.consumers.messages.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed interface SendEmailMessage : Message {

    @Serializable
    @SerialName("send_reset_password_email")
    data class ResetPasswordMessage(
        val token: String
    ) : SendEmailMessage
}
