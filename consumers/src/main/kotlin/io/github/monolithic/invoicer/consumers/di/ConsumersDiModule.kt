package io.github.monolithic.invoicer.consumers.di

import io.github.monolithic.invoicer.consumers.InvoicerMessageConsumerImpl
import io.github.monolithic.invoicer.consumers.strategy.GeneratePdfStrategy
import io.github.monolithic.invoicer.consumers.strategy.GeneratePdfStrategyImpl
import io.github.monolithic.invoicer.consumers.strategy.SendEmailStrategy
import io.github.monolithic.invoicer.consumers.strategy.SendEmailStrategyImpl
import io.github.monolithic.invoicer.consumers.strategy.context.MessageContext
import io.github.monolithic.invoicer.consumers.strategy.context.MessageContextImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val consumersDiModule = DI.Module(name = "ConsumersDiModule") {
    bindProvider<MessageContext> {
        MessageContextImpl(
            generatePdfStrategy = instance(),
            sendEmailStrategy = instance(),
            logger = instance()
        )
    }

    bindProvider<GeneratePdfStrategy> {
        GeneratePdfStrategyImpl(
            invoicePdfService = instance()
        )
    }

    bindProvider<InvoicerMessageConsumerImpl> {
        InvoicerMessageConsumerImpl(
            messageConsumer = instance(),
            messageContext = instance(),
            logger = instance()
        )
    }

    bindProvider<SendEmailStrategy> {
        SendEmailStrategyImpl()
    }
}
