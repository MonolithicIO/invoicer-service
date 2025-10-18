package io.github.monolithic.invoicer.consumers.messages.types

import io.github.monolithic.invoicer.consumers.messages.Message
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

internal sealed interface SendEmailMessage : Message

@Serializable
internal data class ResetPasswordMessage(
    val token: String
) : SendEmailMessage


@Serializable
internal data class PasswordResetCompletedMessage(
    val email: String,
    val updateDate: Instant
) : SendEmailMessage
