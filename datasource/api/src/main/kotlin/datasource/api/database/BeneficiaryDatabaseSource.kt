package datasource.api.database

import datasource.api.model.beneficiary.CreateBeneficiaryData
import datasource.api.model.beneficiary.UpdateBeneficiaryData
import models.beneficiary.BeneficiaryModel
import models.beneficiary.UserBeneficiaries
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
    ): BeneficiaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): BeneficiaryModel?

    suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): UserBeneficiaries

    suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryData
    ): BeneficiaryModel
}

