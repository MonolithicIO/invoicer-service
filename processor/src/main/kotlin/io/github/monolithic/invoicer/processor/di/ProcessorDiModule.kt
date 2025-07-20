package io.github.monolithic.invoicer.processor.di

import io.github.monolithic.invoicer.processor.ProcessBroker
import io.github.monolithic.invoicer.processor.ProcessConsumer
import io.github.monolithic.invoicer.processor.ProcessHandler
import io.github.monolithic.invoicer.processor.context.GeneratePdfCommand
import io.github.monolithic.invoicer.processor.context.GeneratePdfCommandImpl
import io.github.monolithic.invoicer.processor.context.context.ProcessCommander
import io.github.monolithic.invoicer.processor.context.context.ProcessCommanderImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val processorDiModule = DI.Module(name = "ProcessorDiModule") {
    bindProvider<ProcessCommander> {
        ProcessCommanderImpl(
            generatePdfCommand = instance(),
            logger = instance()
        )
    }

    bindProvider<GeneratePdfCommand> {
        GeneratePdfCommandImpl(
            invoicePdfService = instance()
        )
    }

    bindProvider<ProcessHandler> {
        ProcessHandler(
            logger = instance(),
            processCommander = instance(),
            dispatcher = instance()
        )
    }

    bindSingleton<ProcessBroker> { instance<ProcessHandler>() }
    bindProvider<ProcessConsumer> { instance<ProcessHandler>() }

    bindSingleton {
        ProcessHandler(
            logger = instance(),
            processCommander = instance(),
            dispatcher = instance()
        )
    }
}
