package datasource.impl.database

import datasource.api.database.PaymentAccountDatabaseSource
import datasource.impl.entities.PaymentAccountEntity
import datasource.impl.entities.PaymentAccountTable
import datasource.impl.entities.toModel
import models.paymentaccount.PaymentAccountModel
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal class PaymentAccountDatabaseSourceImpl : PaymentAccountDatabaseSource {

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
