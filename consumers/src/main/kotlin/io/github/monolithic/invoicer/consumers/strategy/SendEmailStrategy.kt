package io.github.monolithic.invoicer.consumers.strategy

internal interface SendEmailStrategy {
    suspend fun process()
}

internal class SendEmailStrategyImpl : SendEmailStrategy {
    override suspend fun process() = Unit
}
