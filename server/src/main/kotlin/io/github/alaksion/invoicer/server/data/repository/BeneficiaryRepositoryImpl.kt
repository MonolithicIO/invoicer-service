package io.github.alaksion.invoicer.server.data.repository

import io.github.alaksion.invoicer.server.data.datasource.BeneficiaryDataSource
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

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

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return newSuspendedTransaction {
            dataSource.getById(beneficiaryId)?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return newSuspendedTransaction {
            dataSource.getBySwift(userId, swift)?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel> {
        return newSuspendedTransaction {
            dataSource.getAll(
                userId = userId,
                page = page,
                limit = limit,
            ).map { it.toModel() }
        }
    }
}