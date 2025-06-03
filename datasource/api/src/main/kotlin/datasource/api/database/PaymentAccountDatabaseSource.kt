package datasource.api.database

import models.paymentaccount.PaymentAccountModel

interface PaymentAccountDatabaseSource {
    suspend fun getBySwift(
        swift: String,
    ): PaymentAccountModel?

    suspend fun getByIban(
        iban: String,
    ): PaymentAccountModel?
}