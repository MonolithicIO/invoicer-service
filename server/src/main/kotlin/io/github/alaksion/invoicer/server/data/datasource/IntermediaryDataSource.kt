package io.github.alaksion.invoicer.server.data.datasource

import io.github.alaksion.invoicer.server.data.entities.IntermediaryEntity
import io.github.alaksion.invoicer.server.data.entities.IntermediaryTable
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
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

    fun getById(
        intermediaryId: UUID
    ): IntermediaryEntity?

    fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryEntity?

    fun getAll(
        userId: UUID
    ): List<IntermediaryEntity>
}

internal class IntermediaryDataSourceImpl : IntermediaryDataSource {

    override fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return IntermediaryTable.insertAndGetId { table ->
            table[name] = model.name
            table[iban] = model.iban
            table[swift] = model.swift
            table[bankName] = model.bankName
            table[bankAddress] = model.bankAddress
            table[user] = userId
        }.value.toString()
    }

    override fun delete(userId: UUID, intermediaryId: UUID) {
        IntermediaryTable.deleteWhere {
            user.eq(userId).and(id eq intermediaryId)
        }
    }

    override fun getById(intermediaryId: UUID): IntermediaryEntity? {
        return IntermediaryEntity.find {
            IntermediaryTable.id eq intermediaryId
        }.firstOrNull()
    }

    override fun getBySwift(userId: UUID, swift: String): IntermediaryEntity? {
        return IntermediaryEntity.find {
            (IntermediaryTable.user eq userId).and(IntermediaryTable.swift eq swift)
        }.firstOrNull()
    }

    override fun getAll(userId: UUID): List<IntermediaryEntity> {
        return IntermediaryEntity.find {
            IntermediaryTable.user eq userId
        }.toList()
    }

}