package repository.api.fakes

import models.fixtures.intermediaryModelFixture
import models.fixtures.userIntermediariesFixture
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import java.util.*

class FakeIntermediaryRepository : IntermediaryRepository {

    var getBySwiftResponse: suspend () -> IntermediaryModel? = { null }
    var getByIdResponse: suspend () -> IntermediaryModel? = { intermediaryModelFixture }
    var createResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var getAllResponse: suspend () -> List<IntermediaryModel> = { userIntermediariesFixture }
    var updateResponse: suspend () -> IntermediaryModel = { intermediaryModelFixture }

    var deleteCalls = 0
        private set

    override suspend fun create(userId: UUID, model: CreateIntermediaryModel): String {
        return createResponse()
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        deleteCalls++
    }

    override suspend fun getById(intermediaryId: UUID): IntermediaryModel? {
        return getByIdResponse()
    }

    override suspend fun getBySwift(userId: UUID, swift: String): IntermediaryModel? {
        return getBySwiftResponse()
    }

    override suspend fun getAll(userId: UUID, page: Long, limit: Int): List<IntermediaryModel> {
        return getAllResponse()
    }

    override suspend fun update(
        userId: UUID,
        intermediaryId: UUID,
        model: PartialUpdateIntermediaryModel
    ): IntermediaryModel {
        return updateResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"
    }
}