package repository

import models.paymentaccount.PaymentAccountModel
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.entities.PaymentAccountEntity
import repository.entities.PaymentAccountTable
import repository.entities.toModel

interface PaymentAccountRepository {
    suspend fun getBySwift(swift: String): PaymentAccountModel?
    suspend fun getByIban(iban: String): PaymentAccountModel?
}

internal class PaymentAccountRepositoryImpl : PaymentAccountRepository {

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
}