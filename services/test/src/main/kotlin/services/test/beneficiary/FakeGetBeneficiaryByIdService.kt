package services.test.beneficiary

import kotlinx.datetime.Instant
import models.beneficiary.BeneficiaryModel
import services.api.services.beneficiary.GetBeneficiaryByIdService

class FakeGetBeneficiaryByIdService : GetBeneficiaryByIdService {

    var response: suspend () -> BeneficiaryModel = { DEFAULT_RESPONSE }

    override suspend fun get(beneficiaryId: String, userId: String): BeneficiaryModel {
        return response()
    }

    companion object {
        val DEFAULT_RESPONSE = BeneficiaryModel(
            name = "Beneficiary Name",
            iban = "1234",
            swift = "4321",
            bankAddress = "Bank Address St 1",
            bankName = "Bank of America",
            userId = "36433933-ebaf-42df-83e2-4c684119ccae",
            id = "702c32cd-fc9f-4abb-a022-414104646923",
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z")
        )
    }
}