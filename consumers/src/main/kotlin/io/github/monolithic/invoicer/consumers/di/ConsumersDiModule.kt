package io.github.monolithic.invoicer.consumers.di

import io.github.monolithic.invoicer.consumers.MessageHandlerImpl
import io.github.monolithic.invoicer.consumers.processors.GenerateInvoicePdfProcessor
import io.github.monolithic.invoicer.consumers.processors.GenerateInvoicePdfProcessorImpl
import io.github.monolithic.invoicer.consumers.processors.SendEmailProcessor
import io.github.monolithic.invoicer.consumers.processors.SendEmailProcessorImpl
import io.github.monolithic.invoicer.consumers.processors.context.MessageContext
import io.github.monolithic.invoicer.consumers.processors.context.MessageContextImpl
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val consumersDiModule = DI.Module(name = "ConsumersDiModule") {
    bindProvider<MessageContext> {
        MessageContextImpl(
            generateInvoicePdfProcessor = instance(),
            sendEmailProcessor = instance(),
            logger = instance()
        )
    }

    bindProvider<GenerateInvoicePdfProcessor> {
        GenerateInvoicePdfProcessorImpl(
            invoicePdfService = instance()
        )
    }

    bindProvider<MessageHandlerImpl> {
        MessageHandlerImpl(
            messageConsumer = instance(),
            messageContext = instance(),
            logger = instance(),
            dispatcher = Dispatchers.IO
        )
    }

    bindProvider<SendEmailProcessor> {
        SendEmailProcessorImpl(
            resetPasswordEmailService = instance()
        )
    }
}
