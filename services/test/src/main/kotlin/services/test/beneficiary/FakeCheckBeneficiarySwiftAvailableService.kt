package services.test.beneficiary

import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService

class FakeCheckBeneficiarySwiftAvailableService : CheckBeneficiarySwiftAvailableService {
    var response: suspend () -> Boolean = { true }


    override suspend fun execute(swift: String, userId: String): Boolean {
        return response()
    }
}