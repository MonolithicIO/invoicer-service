package io.github.monolithic.invoicer.foundation.email.providers

import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions
import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import kotlin.coroutines.suspendCoroutine

internal class ResendEmailSender(
    private val secretsProvider: SecretsProvider,
    private val logger: Logger
) : EmailSender {

    private val resend by lazy {
        Resend(secretsProvider.getSecret(SecretKeys.RESEND_API_KEY))
    }

    override suspend fun sendEmail(
        emailBody: String,
        to: String,
        subject: String
    ) = suspendCoroutine<Unit> {
        val emailBody = CreateEmailOptions.builder()
            .from("")
            .to(to)
            .subject(subject)
            .text(emailBody)
            .build()

        runCatching {
            resend.emails().send(emailBody)
        }.fold(
            onSuccess = { response ->
                logger.log(
                    type = ResendEmailSender::class,
                    message = "Resend Email sent to $to with subject $subject. Id: ${response.id}",
                    level = LogLevel.Debug
                )
            },
            onFailure = {
                logger.log(
                    type = ResendEmailSender::class,
                    message = "Resend Email failed to $to",
                    level = LogLevel.Error,
                    throwable = it
                )
            }
        )
    }
}