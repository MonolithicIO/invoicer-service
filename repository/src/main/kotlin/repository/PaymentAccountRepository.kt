package repository

import kotlinx.datetime.Clock
import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.UpdatePaymentAccountModel
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import repository.entities.PaymentAccountEntity
import repository.entities.PaymentAccountTable
import repository.mapper.toModel

interface PaymentAccountRepository {
    suspend fun getBySwift(swift: String): PaymentAccountModel?
    suspend fun getByIban(iban: String): PaymentAccountModel?
    suspend fun update(model: UpdatePaymentAccountModel)
}

internal class PaymentAccountRepositoryImpl(
    private val clock: Clock
) : PaymentAccountRepository {

    override suspend fun getBySwift(swift: String): PaymentAccountModel? {
        return newSuspendedTransaction {
            PaymentAccountEntity.find {
                PaymentAccountTable.swift eq swift
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getByIban(iban: String): PaymentAccountModel? {
        return newSuspendedTransaction {
            PaymentAccountEntity.find {
                PaymentAccountTable.iban eq iban
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun update(model: UpdatePaymentAccountModel) {
        return newSuspendedTransaction {
            PaymentAccountTable.update(
                where = {
                    PaymentAccountTable.id eq model.id
                }
            ) {
                it[iban] = model.iban
                it[swift] = model.swift
                it[bankName] = model.bankName
                it[bankAddress] = model.bankAddress
                it[updatedAt] = clock.now()
            }
        }
    }
}