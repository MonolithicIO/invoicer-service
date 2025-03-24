package datasource.api.database

import datasource.api.model.beneficiary.CreateBeneficiaryData
import datasource.api.model.beneficiary.UpdateBeneficiaryData
import models.beneficiary.BeneficiaryModel
import models.beneficiary.UserBeneficiaries
import models.fixtures.beneficiaryModelFixture
import models.fixtures.userBeneficiariesFixture
import java.util.*

class FakeBeneficiaryDatabaseSource : BeneficiaryDatabaseSource {

    var createResponse: suspend () -> String = { createResponseDefault }
    var deleteResponse: suspend () -> Unit = {}
    var getBydIdResponse: suspend () -> BeneficiaryModel? = { beneficiaryModelFixture }
    var getBySwiftResponse: suspend () -> BeneficiaryModel? = { beneficiaryModelFixture }
    var getAllResponse: suspend () -> UserBeneficiaries = { userBeneficiariesFixture }
    var updateBeneficiaryResponse: suspend () -> BeneficiaryModel = { beneficiaryModelFixture }


    override suspend fun create(userId: UUID, model: CreateBeneficiaryData): String {
        return createResponse()
    }

    override suspend fun delete(userId: UUID, beneficiaryId: UUID) {
        return deleteResponse()
    }

    override suspend fun getById(beneficiaryId: UUID): BeneficiaryModel? {
        return getBydIdResponse()
    }

    override suspend fun getBySwift(userId: UUID, swift: String): BeneficiaryModel? {
        return getBySwiftResponse()
    }

    override suspend fun getAll(userId: UUID, page: Long, limit: Int): UserBeneficiaries {
        return getAllResponse()
    }

    override suspend fun update(
        userId: UUID,
        beneficiaryId: UUID,
        model: UpdateBeneficiaryData
    ): BeneficiaryModel {
        return updateBeneficiaryResponse()
    }

    companion object {
        val createResponseDefault = "2bd1667c-459c-44a4-a74e-f4724c1ef8ab"
    }

}

