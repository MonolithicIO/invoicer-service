package repository.api.repository

import entities.BeneficiaryEntity
import entities.BeneficiaryTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.updateReturning
import repository.api.mapper.toModel
import services.api.model.beneficiary.BeneficiaryModel
import services.api.model.beneficiary.CreateBeneficiaryModel
import services.api.model.beneficiary.UpdateBeneficiaryModel
import services.api.repository.BeneficiaryRepository
import utils.date.api.DateProvider
import java.util.UUID

internal class BeneficiaryRepositoryImpl(
    private val dateProvider: DateProvider
) : BeneficiaryRepository {

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return newSuspendedTransaction {
            BeneficiaryTable.insertAndGetId { table ->
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

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.id eq beneficiaryId) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return newSuspendedTransaction {
            BeneficiaryEntity.find {
                (BeneficiaryTable.user eq userId).and(BeneficiaryTable.swift eq swift) and (BeneficiaryTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel> {
        return newSuspendedTransaction {
            val query = BeneficiaryTable
                .selectAll()
                .where {
                    BeneficiaryTable.user eq userId and (BeneficiaryTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            BeneficiaryEntity.wrapRows(query)
                .toList()
                .map { it.toModel() }
        }
    }

    override suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryModel
    ): BeneficiaryModel {
        return newSuspendedTransaction {
            BeneficiaryTable.updateReturning(
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
                it[updatedAt] = dateProvider.now()
            }.map {
                BeneficiaryEntity.wrapRow(it)
            }.first().toModel()
        }
    }
}