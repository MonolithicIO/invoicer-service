package io.github.monolithic.invoicer.foundation.email

interface EmailSender {
    suspend fun sendEmail(
        emailBody: String,
        to: String,
        subject: String
    )

    suspend fun sendTemplateEmail(
        template: String,
        to: String,
        subject: String
    )
}