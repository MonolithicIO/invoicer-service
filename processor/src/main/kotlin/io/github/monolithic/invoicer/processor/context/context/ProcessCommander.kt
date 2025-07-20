package io.github.monolithic.invoicer.processor.context.context

import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.processor.context.GeneratePdfCommand
import io.github.monolithic.invoicer.processor.process.Process

internal interface ProcessCommander {
    suspend fun handleProcess(process: Process)
}

internal class ProcessCommanderImpl(
    private val generatePdfCommand: GeneratePdfCommand,
    private val logger: Logger
) : ProcessCommander {
    override suspend fun handleProcess(process: Process) {
        runCatching {
            when (process) {
                is Process.InvoicePdfProcess -> generatePdfCommand.process(
                    process = process
                )
            }
        }.onFailure { error ->
            logger.log(
                type = ProcessCommanderImpl::class,
                message = "Failed to process message: $process",
                level = LogLevel.Warn,
                throwable = error
            )
        }
    }
}
