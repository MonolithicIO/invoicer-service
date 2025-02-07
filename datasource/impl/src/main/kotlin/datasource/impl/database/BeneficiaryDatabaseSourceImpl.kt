package datasource.impl.database

import datasource.api.database.BeneficiaryDatabaseSource
import datasource.api.model.beneficiary.CreateBeneficiaryData
import datasource.api.model.beneficiary.UpdateIntermediaryData
import entities.BeneficiaryEntity
import entities.BeneficiaryTable
import entities.UserBeneficiariesEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.date.impl.DateProvider
import java.util.*

internal class BeneficiaryDatabaseSourceImpl(
    private val dateProvider: DateProvider
) : BeneficiaryDatabaseSource {

    override suspend fun create(userId: UUID, model: CreateBeneficiaryData): String {
        return newSuspendedTransaction {
            BeneficiaryTable.insertAndGetId { table ->
                table[name] = model.name
                table[iban] = model.iban
                table[swift] = model.swift
                table[bankName] = model.bankName
                table[bankAddress] = model.bankAddress
                table[user] = userId
                table[createdAt] = dateProvider.currentInstant()
                table[updatedAt] = dateProvider.currentInstant()
            }.value.toString()
        }
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        return newSuspendedTransaction {
            BeneficiaryTable.update(
                where = {
                    BeneficiaryTable.user.eq(userId).and(BeneficiaryTable.id eq beneficiaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryEntity? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.id eq beneficiaryId) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryEntity? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.user eq userId).and(BeneficiaryTable.swift eq swift) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiariesEntity {
        return newSuspendedTransaction {
            val query = BeneficiaryTable
                .selectAll()
                .where {
                    BeneficiaryTable.user eq userId and (BeneficiaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            val count = query.count()
            val currentOffset = page * limit

            val nextPage = if (count > currentOffset) {
                (count - currentOffset) / limit
            } else {
                null
            }

            val result = BeneficiaryEntity.wrapRows(query)
                .toList()

            UserBeneficiariesEntity(
                items = result,
                nextPage = nextPage,
                itemCount = count
            )
        }
    }

    override suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateIntermediaryData
    ): BeneficiaryEntity {
        return newSuspendedTransaction {
            BeneficiaryTable.updateReturning(
                where = {
                    BeneficiaryTable.user eq userId
                    BeneficiaryTable.id eq beneficiaryId
                }
            ) { table ->
                model.name?.let { table[name] = it }
                model.iban?.let { table[iban] = it }
                model.swift?.let { table[swift] = it }
                model.bankName?.let { table[bankName] = it }
                model.bankAddress?.let { table[bankAddress] = it }
                table[updatedAt] = dateProvider.currentInstant()
            }.map {
                BeneficiaryEntity.wrapRow(it)
            }.first()
        }
    }
}