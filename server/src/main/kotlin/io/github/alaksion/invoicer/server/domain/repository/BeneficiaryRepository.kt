package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.data.datasource.BeneficiaryDataSource
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
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
        userId: UUID,
        beneficiaryId: UUID
    ): BeneficiaryModel?

    suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): BeneficiaryModel?

    suspend fun getAll(
        userId: UUID
    ): List<BeneficiaryModel>
}

internal class BeneficiaryRepositoryImpl(
    private val dataSource: BeneficiaryDataSource
) : BeneficiaryRepository {

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return newSuspendedTransaction {
            dataSource.create(userId, model)
        }
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        return newSuspendedTransaction {
            dataSource.delete(userId, beneficiaryId)
        }
    }

    override suspend fun getById(userId: UUID, beneficiaryId: UUID): BeneficiaryModel? {
        return newSuspendedTransaction {
            dataSource.getById(userId, beneficiaryId)?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return newSuspendedTransaction {
            dataSource.getBySwift(userId, swift)?.toModel()
        }
    }

    override suspend fun getAll(userId: UUID): List<BeneficiaryModel> {
        return newSuspendedTransaction {
            dataSource.getAll(userId).map { it.toModel() }
        }
    }
}