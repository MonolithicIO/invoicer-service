package io.github.alaksion.invoicer.server.data.repository

import io.github.alaksion.invoicer.server.data.datasource.IntermediaryDataSource
import io.github.alaksion.invoicer.server.data.extensions.toModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.UpdateIntermediaryModel
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
            dataSource.getUserIntermediaryById(intermediaryId)?.toModel()
        }
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return newSuspendedTransaction {
            dataSource.getUserIntermediaryBySwift(userId, swift)?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryModel> {
        return newSuspendedTransaction {
            dataSource.getUserIntermediaries(
                userId = userId,
                page = page,
                limit = limit
            ).map { it.toModel() }
        }
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: UpdateIntermediaryModel
    ): IntermediaryModel {
        return newSuspendedTransaction {
            dataSource.update(userId, intermediaryId, model).toModel()
        }
    }
}