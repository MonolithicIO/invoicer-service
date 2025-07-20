package io.github.monolithic.invoicer.processor

import io.github.monolithic.invoicer.foundation.log.LogLevel
import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.processor.context.context.ProcessCommander
import io.github.monolithic.invoicer.processor.process.Process
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

interface ProcessConsumer {
    fun startObserving()
    fun close()
}

interface ProcessBroker {
    suspend fun submitProcess(process: Process)
}

internal class ProcessHandler(
    private val logger: Logger,
    private val processCommander: ProcessCommander,
    dispatcher: CoroutineDispatcher
) : ProcessConsumer, ProcessBroker {

    private val processorScope = CoroutineScope(dispatcher)

    private val _processFlow = MutableSharedFlow<io.github.monolithic.invoicer.processor.process.Process>()

    override fun startObserving() {
        processorScope.launch {
            _processFlow.collect { process ->
                runProcessCatching(process)
            }
        }
    }

    private suspend fun runProcessCatching(process: Process) {
        runCatching {
            when (process) {
                is Process.InvoicePdfProcess -> processCommander.handleProcess(process)
            }
        }.onFailure {
            logger.log(
                type = ProcessHandler::class,
                message = "Process commander failed to handle process: $process",
                level = LogLevel.Error,
                throwable = it
            )
        }
    }

    override fun close() {
        processorScope.cancel()
    }

    override suspend fun submitProcess(process: Process) {
        logger.log(
            type = ProcessHandler::class,
            level = LogLevel.Debug,
            message = "Submitting process: $process"
        )
        _processFlow.emit(process)
    }
}
