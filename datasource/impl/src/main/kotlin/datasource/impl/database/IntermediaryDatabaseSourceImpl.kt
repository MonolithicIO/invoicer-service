package datasource.impl.database

import datasource.api.database.IntermediaryDatabaseSource
import datasource.api.model.intermediary.CreateIntermediaryData
import datasource.api.model.intermediary.UpdateIntermediaryData
import entities.BeneficiaryTable.user
import entities.IntermediaryEntity
import entities.IntermediaryTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.date.impl.DateProvider
import java.util.*

internal class IntermediaryDatabaseSourceImpl(
    private val dateProvider: DateProvider
) : IntermediaryDatabaseSource {

    override suspend fun create(userId: UUID, model: CreateIntermediaryData): String {
        return newSuspendedTransaction {
            IntermediaryTable.insertAndGetId { table ->
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

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return newSuspendedTransaction {
            IntermediaryTable.update(
                where = {
                    user.eq(userId).and(IntermediaryTable.id eq intermediaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryEntity? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (IntermediaryTable.id eq intermediaryId) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryEntity? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (user eq userId).and(IntermediaryTable.swift eq swift) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryEntity> {
        return newSuspendedTransaction {
            val query = IntermediaryTable
                .selectAll()
                .where {
                    user eq userId and (IntermediaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            IntermediaryEntity.wrapRows(query)
                .toList()
        }
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: UpdateIntermediaryData
    ): IntermediaryEntity {
        return newSuspendedTransaction {
            IntermediaryTable.updateReturning(
                where = {
                    user eq userId
                    IntermediaryTable.id eq intermediaryId
                }
            ) { table ->
                model.name?.let { table[name] = it }
                model.iban?.let { table[iban] = it }
                model.swift?.let { table[swift] = it }
                model.bankName?.let { table[bankName] = it }
                model.bankAddress?.let { table[bankAddress] = it }
                table[updatedAt] = dateProvider.currentInstant()
            }.map {
                IntermediaryEntity.wrapRow(it)
            }.first()
        }
    }
}