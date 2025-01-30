package repository.test.repository

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.intermediary.CreateIntermediaryModel
import models.intermediary.IntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import java.util.*

class FakeIntermediaryRepository : IntermediaryRepository {

    var getBySwiftResponse: suspend () -> IntermediaryModel? = { null }
    var getByIdResponse: suspend () -> IntermediaryModel? = { DEFAULT_INTERMEDIARY }
    var createResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var getAllResponse: suspend () -> List<IntermediaryModel> = { listOf() }
    lateinit var updateResponse: suspend () -> IntermediaryModel

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

    override suspend fun update(userId: UUID, intermediaryId: UUID, model: UpdateIntermediaryModel): IntermediaryModel {
        return updateResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"

        val DEFAULT_INTERMEDIARY = IntermediaryModel(
            name = "Name",
            iban = "Iban",
            swift = "Swift,",
            bankName = "bank name",
            bankAddress = "bank address",
            userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
            id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z")
        )
    }
}