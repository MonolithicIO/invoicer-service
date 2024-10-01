package services.api.repository

import services.api.model.beneficiary.BeneficiaryModel
import services.api.model.beneficiary.CreateBeneficiaryModel
import services.api.model.beneficiary.UpdateBeneficiaryModel
import java.util.UUID

interface BeneficiaryRepository {
    suspend fun create(
        userId: UUID,
        model: CreateBeneficiaryModel
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
    ): List<BeneficiaryModel>

    suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryModel
    ): BeneficiaryModel
}