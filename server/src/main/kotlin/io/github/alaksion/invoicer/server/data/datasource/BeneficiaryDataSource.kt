package io.github.alaksion.invoicer.server.data.datasource

import entities.BeneficiaryEntity
import entities.BeneficiaryTable
import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.UpdateBeneficiaryModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.updateReturning
import java.util.UUID

internal interface BeneficiaryDataSource {
    fun create(
        userId: UUID,
        model: CreateBeneficiaryModel
    ): String

    fun delete(
        userId: UUID,
        beneficiaryId: UUID
    )

    fun getById(
        beneficiaryId: UUID
    ): BeneficiaryEntity?

    fun getBySwift(
        userId: UUID,
        swift: String
    ): BeneficiaryEntity?

    fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<BeneficiaryEntity>

    fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryModel
    ): BeneficiaryEntity
}

internal class BeneficiaryDataSourceImpl : BeneficiaryDataSource {

    override fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return BeneficiaryTable.insertAndGetId { table ->
            table[name] = model.name
            table[iban] = model.iban
            table[swift] = model.swift
            table[bankName] = model.bankName
            table[bankAddress] = model.bankAddress
            table[user] = userId
        }.value.toString()
    }

    override fun delete(userId: UUID, beneficiaryId: UUID) {
        BeneficiaryTable.deleteWhere {
            user.eq(userId).and(id eq beneficiaryId)
        }
    }

    override fun getById(beneficiaryId: UUID): BeneficiaryEntity? {
        return BeneficiaryEntity.find {
            BeneficiaryTable.id eq beneficiaryId
        }.firstOrNull()
    }

    override fun getBySwift(userId: UUID, swift: String): BeneficiaryEntity? {
        return BeneficiaryEntity.find {
            (BeneficiaryTable.user eq userId).and(BeneficiaryTable.swift eq swift)
        }.firstOrNull()
    }

    override fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<BeneficiaryEntity> {
        val query = BeneficiaryTable
            .selectAll()
            .where {
                BeneficiaryTable.user eq userId
            }
            .limit(n = limit, offset = page * limit)

        return BeneficiaryEntity.wrapRows(query).toList()
    }

    override fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryModel
    ): BeneficiaryEntity {
        return BeneficiaryTable.updateReturning(
            where = {
                BeneficiaryTable.user eq userId
                BeneficiaryTable.id eq beneficiaryId
            }
        ) {
            it[name] = model.name
            it[iban] = model.iban
            it[swift] = model.swift
            it[bankName] = model.bankName
            it[bankAddress] = model.bankAddress
        }.map {
            BeneficiaryEntity.wrapRow(it)
        }.first()
    }
}