package io.github.monolithic.invoicer.services.payaccount

import io.github.monolithic.invoicer.repository.PaymentAccountRepository

interface CheckPayAccountDocumentInUseService {

    suspend fun checkSwiftInUse(swift: String): Boolean

    suspend fun checkIbanInUse(iban: String): Boolean
}

internal class CheckPayAccountDocumentInUseServiceImpl(
    private val paymentAccountRepository: PaymentAccountRepository
) : CheckPayAccountDocumentInUseService {

    override suspend fun checkSwiftInUse(swift: String): Boolean {
        return paymentAccountRepository.getBySwift(swift) != null
    }

    override suspend fun checkIbanInUse(iban: String): Boolean {
        return paymentAccountRepository.getByIban(iban) != null
    }
}
