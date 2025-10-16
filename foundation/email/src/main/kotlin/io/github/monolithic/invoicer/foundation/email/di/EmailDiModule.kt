package io.github.monolithic.invoicer.foundation.email.di

import io.github.monolithic.invoicer.foundation.email.EmailSender
import io.github.monolithic.invoicer.foundation.email.providers.ResendEmailSender
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val emailDiModule = DI.Module("email") {
    bindProvider<EmailSender> {
        ResendEmailSender(
            secretsProvider = instance(),
            logger = instance()
        )
    }
}
