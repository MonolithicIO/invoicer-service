package datasource.api.database

import datasource.api.model.beneficiary.CreateBeneficiaryData
import datasource.api.model.beneficiary.UpdateIntermediaryData
import entities.BeneficiaryEntity
import entities.UserBeneficiariesEntity
import java.util.*

interface BeneficiaryDatabaseSource {
    suspend fun create(
        userId: UUID,
        model: CreateBeneficiaryData
    ): String

    suspend fun delete(
        userId: UUID,
        beneficiaryId: UUID
    )

    suspend fun getById(
        beneficiaryId: UUID
    ): BeneficiaryEntity?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): BeneficiaryEntity?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiariesEntity

    suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateIntermediaryData
    ): BeneficiaryEntity
}

