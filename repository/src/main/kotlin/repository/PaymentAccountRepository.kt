package repository

import java.util.*
import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.UpdatePaymentAccountModel
import repository.datasource.PaymentAccountDataSource

interface PaymentAccountRepository {
    suspend fun getBySwift(swift: String): PaymentAccountModel?
    suspend fun getByIban(iban: String): PaymentAccountModel?
    suspend fun update(model: UpdatePaymentAccountModel)
    suspend fun getById(id: UUID): PaymentAccountModel?
    suspend fun delete(id: UUID)
}

internal class PaymentAccountRepositoryImpl(
    private val dataSource: PaymentAccountDataSource
) : PaymentAccountRepository {

    override suspend fun getBySwift(swift: String): PaymentAccountModel? {
        return dataSource.getBySwift(swift = swift)
    }

    override suspend fun getByIban(iban: String): PaymentAccountModel? {
        return dataSource.getByIban(iban = iban)
    }

    override suspend fun update(model: UpdatePaymentAccountModel) {
        return dataSource.update(model = model)
    }

    override suspend fun getById(id: UUID): PaymentAccountModel? {
        return dataSource.getById(id = id)
    }

    override suspend fun delete(id: UUID) {
        dataSource.delete(id = id)
    }
}
