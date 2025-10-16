package io.github.monolithic.invoicer.foundation.email.providers

import com.resend.Resend
import com.resend.services.emails.model.CreateEmailOptions
import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.env.secrets.SecretKeys
import io.github.monolithic.invoicer.foundation.env.secrets.SecretsProvider
import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
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
    ) = suspendCoroutine { continuation ->
        val emailBody = CreateEmailOptions.builder()
            .from(INVOICER_EMAIL)
            .to(to)
            .subject(subject)
            .text(emailBody)
            .build();

        continuation.sendResendEmail(emailBody)
    }

    override suspend fun sendTemplateEmail(template: String, to: String, subject: String) =
        suspendCoroutine { continuation ->
            val emailBody = CreateEmailOptions.builder()
                .from(INVOICER_EMAIL)
                .to(to)
                .subject(subject)
                .html(template)
                .build();

            continuation.sendResendEmail(emailBody)
        }

    private fun Continuation<Unit>.sendResendEmail(
        configuration: CreateEmailOptions
    ) {
        runCatching {
            resend.emails().send(configuration)
        }.fold(
            onSuccess = { response ->
                logger.log(
                    type = ResendEmailSender::class,
                    message = "Resend Email sent: Id: ${response.id}",
                    level = LogLevel.Debug
                )
                resume(Unit)
            },
            onFailure = {
                logger.log(
                    type = ResendEmailSender::class,
                    message = "Resend Email failed",
                    level = LogLevel.Error,
                    throwable = it
                )
                resume(Unit)
            }
        )
    }

    companion object {
        const val INVOICER_EMAIL = "Invoicer <invoicer@monolithic-io.com>"
    }
}
