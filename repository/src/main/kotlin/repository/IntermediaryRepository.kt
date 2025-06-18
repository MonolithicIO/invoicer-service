package repository

import foundation.cache.CacheHandler
import kotlinx.datetime.Clock
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.entities.legacy.IntermediaryEntity
import repository.entities.legacy.IntermediaryTable
import repository.mapper.toModel
import java.util.*

interface IntermediaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    suspend fun delete(
        userId: UUID,
        intermediaryId: UUID
    )

    suspend fun getById(
        intermediaryId: UUID
    ): IntermediaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryModel>

    suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel
}

internal class IntermediaryRepositoryImpl(
    private val clock: Clock,
    private val cacheHandler: CacheHandler
) : IntermediaryRepository {

    override suspend fun create(userId: UUID, model: CreateIntermediaryModel): String {
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
        newSuspendedTransaction {
            IntermediaryTable.update(
                where = {
                    IntermediaryTable.user.eq(userId).and(IntermediaryTable.id eq intermediaryId)
                }
            ) {
                it[isDeleted] = true
            }
        }.also {
            cacheHandler.delete(intermediaryId.toString())
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        val cached = cacheHandler.get(
            key = intermediaryId.toString(),
            serializer = IntermediaryModel.serializer()
        )

        if (cached != null) return cached

        val databaseResult = newSuspendedTransaction {
            IntermediaryEntity.find {
                (IntermediaryTable.id eq intermediaryId) and (IntermediaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }

        return databaseResult?.also {
            cacheHandler.set(
                key = intermediaryId.toString(),
                value = it,
                serializer = IntermediaryModel.serializer(),
                ttlSeconds = CACHE_TTL_SECONDS
            )
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
        model: PartialUpdateIntermediaryModel
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
        }.also {
            cacheHandler.delete(intermediaryId.toString())
        }
    }

    companion object {
        const val CACHE_TTL_SECONDS = 600L
    }
}