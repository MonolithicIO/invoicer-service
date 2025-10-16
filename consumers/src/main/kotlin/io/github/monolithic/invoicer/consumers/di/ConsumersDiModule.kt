package io.github.monolithic.invoicer.consumers.di

import io.github.monolithic.invoicer.consumers.MessageHandlerImpl
import io.github.monolithic.invoicer.consumers.strategy.GenerateInvoicePdfProcessor
import io.github.monolithic.invoicer.consumers.strategy.GenerateInvoicePdfProcessorImpl
import io.github.monolithic.invoicer.consumers.strategy.SendEmailProcessor
import io.github.monolithic.invoicer.consumers.strategy.SendEmailProcessorImpl
import io.github.monolithic.invoicer.consumers.strategy.context.MessageContext
import io.github.monolithic.invoicer.consumers.strategy.context.MessageContextImpl
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
        SendEmailProcessorImpl()
    }
}
