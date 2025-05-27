package datasource.impl.database

import datasource.api.database.IntermediaryDatabaseSource
import datasource.api.model.intermediary.CreateIntermediaryData
import datasource.api.model.intermediary.UpdateIntermediaryData
import datasource.impl.entities.legacy.IntermediaryEntity
import datasource.impl.entities.legacy.IntermediaryTable
import datasource.impl.mapper.toModel
import kotlinx.datetime.Clock
import models.intermediary.IntermediaryModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

internal class IntermediaryDatabaseSourceImpl(
    private val clock: Clock
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
                table[createdAt] = clock.now()
                table[updatedAt] = clock.now()
            }.value.toString()
        }
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return newSuspendedTransaction {
            IntermediaryTable.update(
                where = {
                    IntermediaryTable.user.eq(userId).and(IntermediaryTable.id eq intermediaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (IntermediaryTable.id eq intermediaryId) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return newSuspendedTransaction {
            IntermediaryEntity.find {
                (IntermediaryTable.user eq userId).and(IntermediaryTable.swift eq swift) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryModel> {
        return newSuspendedTransaction {
            val query = IntermediaryTable
                .selectAll()
                .where {
                    IntermediaryTable.user eq userId and (IntermediaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            IntermediaryEntity.wrapRows(query)
                .toList()
                .map { it.toModel() }
        }
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: UpdateIntermediaryData
    ): IntermediaryModel {
        return newSuspendedTransaction {
            IntermediaryTable.updateReturning(
                where = {
                    IntermediaryTable.user eq userId
                    IntermediaryTable.id eq intermediaryId
                }
            ) { table ->
                model.name?.let { table[name] = it }
                model.iban?.let { table[iban] = it }
                model.swift?.let { table[swift] = it }
                model.bankName?.let { table[bankName] = it }
                model.bankAddress?.let { table[bankAddress] = it }
                table[updatedAt] = clock.now()
            }.map {
                IntermediaryEntity.wrapRow(it).toModel()
            }.first()
        }
    }
}