package domains.intermediary.data.api.datasource

import domains.intermediary.domain.api.model.CreateIntermediaryModel
import domains.intermediary.domain.api.model.UpdateIntermediaryModel
import entities.IntermediaryEntity
import entities.IntermediaryTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.updateReturning
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

    override fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<IntermediaryEntity> {
        val query = IntermediaryTable
            .selectAll()
            .where {
                IntermediaryTable.user eq userId
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
        }.map {
            IntermediaryEntity.wrapRow(it)
        }.first()
    }

}