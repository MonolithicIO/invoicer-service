package datasource.api.database

import datasource.api.model.intermediary.CreateIntermediaryData
import datasource.api.model.intermediary.UpdateIntermediaryData
import models.fixtures.intermediaryModelFixture
import models.fixtures.userIntermediariesFixture
import models.intermediary.IntermediaryModel
import java.util.*

class FakeIntermediaryDatabaseSource : IntermediaryDatabaseSource {

    var createResponse: suspend () -> String = { createResponseDefault }
    var deleteResponse: suspend () -> Unit = {}
    var getByIdResponse: suspend () -> IntermediaryModel? = { intermediaryModelFixture }
    var getBySwiftResponse: suspend () -> IntermediaryModel? = { intermediaryModelFixture }
    var getAllResponse: suspend () -> List<IntermediaryModel> = { userIntermediariesFixture }
    var updateResponse: suspend () -> IntermediaryModel = { intermediaryModelFixture }

    override suspend fun create(userId: UUID, model: CreateIntermediaryData): String {
        return createResponse()
    }

    override suspend fun delete(userId: UUID, intermediaryId: UUID) {
        return deleteResponse()
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

    override suspend fun update(userId: UUID, intermediaryId: UUID, model: UpdateIntermediaryData): IntermediaryModel {
        return updateResponse()
    }

    companion object {
        val createResponseDefault = "2bd1667c-459c-44a4-a74e-f4724c1ef8ab"
    }
}