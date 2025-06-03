package repository

import datasource.api.database.PaymentAccountDatabaseSource
import models.paymentaccount.PaymentAccountModel

interface PaymentAccountRepository {
    suspend fun getBySwift(swift: String): PaymentAccountModel?
    suspend fun getByIban(iban: String): PaymentAccountModel?
}

internal class PaymentAccountRepositoryImpl(
    private val databaseSource: PaymentAccountDatabaseSource
) : PaymentAccountRepository {

    override suspend fun getBySwift(swift: String): PaymentAccountModel? {
        return databaseSource.getBySwift(swift)
    }

    override suspend fun getByIban(iban: String): PaymentAccountModel? {
        return databaseSource.getByIban(iban)
    }
}