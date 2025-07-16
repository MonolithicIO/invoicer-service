package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.PaymentAccountEntity
import io.github.monolithic.invoicer.repository.entities.PaymentAccountTable
import io.github.monolithic.invoicer.repository.mapper.toModel
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountModel
import io.github.monolithic.invoicer.models.paymentaccount.UpdatePaymentAccountModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

interface PaymentAccountDataSource {
    suspend fun getBySwift(swift: String): PaymentAccountModel?
    suspend fun getByIban(iban: String): PaymentAccountModel?
    suspend fun update(model: UpdatePaymentAccountModel)
    suspend fun getById(id: UUID): PaymentAccountModel?
    suspend fun delete(id: UUID)
}

internal class PaymentAccountDataSourceImpl(
    private val clock: Clock
) : PaymentAccountDataSource {

    override suspend fun getBySwift(swift: String): PaymentAccountModel? {
        return newSuspendedTransaction {
            PaymentAccountEntity.Companion.find {
                PaymentAccountTable.swift eq swift
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getByIban(iban: String): PaymentAccountModel? {
        return newSuspendedTransaction {
            PaymentAccountEntity.Companion.find {
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

    override suspend fun getById(id: UUID): PaymentAccountModel? {
        return newSuspendedTransaction {
            PaymentAccountEntity.Companion.findById(id)?.toModel()
        }
    }

    override suspend fun delete(id: UUID) {
        newSuspendedTransaction {
            PaymentAccountTable.deleteWhere {
                PaymentAccountTable.id eq id
            }
        }
    }
}
