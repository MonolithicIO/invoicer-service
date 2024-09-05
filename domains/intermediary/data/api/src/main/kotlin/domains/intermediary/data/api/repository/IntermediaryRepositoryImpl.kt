package domains.intermediary.data.api.repository


import domains.intermediary.data.api.datasource.IntermediaryDataSource
import domains.intermediary.data.api.extensions.toModel
import domains.intermediary.domain.api.model.CreateIntermediaryModel
import domains.intermediary.domain.api.model.IntermediaryModel
import domains.intermediary.domain.api.model.UpdateIntermediaryModel
import domains.intermediary.domain.api.repository.IntermediaryRepository
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

    override suspend fun getBySwift(
        userId: UUID,
        swift: String
    ): IntermediaryModel? {
        return newSuspendedTransaction {
            dataSource.getBySwift(userId, swift)?.toModel()
        }
    }

    override suspend fun getAll(
        userId: UUID,
        page: Long,
        limit: Int
    ): List<IntermediaryModel> {
        return newSuspendedTransaction {
            dataSource.getAll(
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