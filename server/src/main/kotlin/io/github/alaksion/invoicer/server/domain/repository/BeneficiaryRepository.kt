package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import java.util.UUID

internal interface BeneficiaryRepository {
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
}