package io.github.alaksion.invoicer.server.data.datasource

import entities.IntermediaryEntity
import entities.IntermediaryTable
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.UpdateIntermediaryModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.updateReturning
import utils.date.api.DateProvider
import java.util.UUID

internal interface IntermediaryDataSource {
    fun create(
        userId: UUID,
        model: CreateIntermediaryModel
    ): String

    fun delete(
        userId: UUID,
        intermediaryId: UUID
    )

    fun getUserIntermediaryById(
        intermediaryId: UUID
    ): IntermediaryEntity?

    fun getUserIntermediaryBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryEntity?

    fun getUserIntermediaries(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryEntity>

    fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateIntermediaryModel
    ): IntermediaryEntity
}

internal class IntermediaryDataSourceImpl(
    private val dateProvider: DateProvider
) : IntermediaryDataSource {

    override fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return IntermediaryTable.insertAndGetId { table ->
            table[name] = model.name
            table[iban] = model.iban
            table[swift] = model.swift
            table[bankName] = model.bankName
            table[bankAddress] = model.bankAddress
            table[user] = userId
            table[createdAt] = dateProvider.now()
            table[updatedAt] = dateProvider.now()
        }.value.toString()
    }

    override fun delete(userId: UUID, intermediaryId: UUID) {
        IntermediaryTable.update(
            where = {
                IntermediaryTable.user.eq(userId).and(IntermediaryTable.id eq intermediaryId)
            }
        ) {
            it[isDeleted] = true
        }
    }

    override fun getUserIntermediaryById(intermediaryId: UUID): IntermediaryEntity? {
        return IntermediaryEntity.find {
            (IntermediaryTable.id eq intermediaryId) and (IntermediaryTable.isDeleted eq false)
        }.firstOrNull()
    }

    override fun getUserIntermediaryBySwift(userId: UUID, swift: String): IntermediaryEntity? {
        return IntermediaryEntity.find {
            (IntermediaryTable.user eq userId).and(IntermediaryTable.swift eq swift) and (IntermediaryTable.isDeleted eq false)
        }.firstOrNull()
    }

    override fun getUserIntermediaries(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryEntity> {
        val query = IntermediaryTable
            .selectAll()
            .where {
                IntermediaryTable.user eq userId and (IntermediaryTable.isDeleted eq false)
            }
            .limit(n = limit, offset = page * limit)

        return IntermediaryEntity.wrapRows(query).toList()
    }

    override fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateIntermediaryModel
    ): IntermediaryEntity {
        return IntermediaryTable.updateReturning(
            where = {
                IntermediaryTable.user eq userId
                IntermediaryTable.id eq beneficiaryId
            }
        ) {
            it[name] = model.name
            it[iban] = model.iban
            it[swift] = model.swift
            it[bankName] = model.bankName
            it[bankAddress] = model.bankAddress
            it[updatedAt] = dateProvider.now()
        }.map {
            IntermediaryEntity.wrapRow(it)
        }.first()
    }

}