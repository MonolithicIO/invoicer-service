package io.github.monolithic.invoicer.processor.di

import io.github.monolithic.invoicer.processor.process.ProcessBroker
import io.github.monolithic.invoicer.processor.process.ProcessConsumer
import io.github.monolithic.invoicer.processor.process.ProcessHandler
import io.github.monolithic.invoicer.processor.commander.commands.GeneratePdfCommand
import io.github.monolithic.invoicer.processor.commander.commands.GeneratePdfCommandImpl
import io.github.monolithic.invoicer.processor.commander.ProcessCommander
import io.github.monolithic.invoicer.processor.commander.ProcessCommanderImpl
import kotlinx.coroutines.Dispatchers
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

    bindSingleton<ProcessBroker> { instance<ProcessHandler>() }
    bindProvider<ProcessConsumer> { instance<ProcessHandler>() }

    bindSingleton {
        ProcessHandler(
            logger = instance(),
            processCommander = instance(),
            dispatcher = Dispatchers.IO
        )
    }
}
