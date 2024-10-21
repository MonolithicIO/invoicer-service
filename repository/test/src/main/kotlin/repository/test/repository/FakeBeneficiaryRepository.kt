package repository.test.repository

import models.beneficiary.BeneficiaryModel
import models.beneficiary.CreateBeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import java.util.*

class FakeBeneficiaryRepository : BeneficiaryRepository {

    var createResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var getAllResponse: suspend () -> List<BeneficiaryModel> = { listOf() }
    lateinit var getByIdResponse: suspend () -> BeneficiaryModel
    var getBySwiftResponse: suspend () -> BeneficiaryModel? = { null }
    lateinit var updateBeneficiaryResponse: suspend () -> BeneficiaryModel

    var deleteCalls = 0
        private set

    override suspend fun create(userId: UUID, model: CreateBeneficiaryModel): String {
        return createResponse()
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        deleteCalls++
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return getByIdResponse()
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return getBySwiftResponse()
    }

    override suspend fun getAll(userId: UUID, page: Long, limit: Int): List<BeneficiaryModel> {
        return getAllResponse()
    }

    override suspend fun update(userId: UUID, beneficiaryId: UUID, model: UpdateBeneficiaryModel): BeneficiaryModel {
        return updateBeneficiaryResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"
    }
}