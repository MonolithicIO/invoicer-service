package io.github.alaksion.invoicer.server.data.repository

import io.github.alaksion.invoicer.server.data.datasource.IntermediaryDataSource
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

internal class IntermediaryRepositoryImpl(
    private val dataSource: IntermediaryDataSource
) : IntermediaryRepository {

    override suspend fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return newSuspendedTransaction {
            dataSource.create(userId, model)
        }
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return newSuspendedTransaction {
            dataSource.delete(userId, intermediaryId)
        }
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        return newSuspendedTransaction {
            dataSource.getById(intermediaryId)?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return newSuspendedTransaction {
            dataSource.getBySwift(userId, swift)?.toModel()
        }
    }

    override suspend fun getAll(userId: UUID): List<IntermediaryModel> {
        return newSuspendedTransaction {
            dataSource.getAll(userId).map { it.toModel() }
        }
    }
}