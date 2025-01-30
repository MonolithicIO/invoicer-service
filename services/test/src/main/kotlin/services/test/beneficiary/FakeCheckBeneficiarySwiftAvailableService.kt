package services.test.beneficiary

import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService

class FakeCheckBeneficiarySwiftAvailableService : CheckBeneficiarySwiftAvailableService {
    var response: suspend () -> Boolean = { true }

    var calls = 0
        private set

    override suspend fun execute(swift: String, userId: String): Boolean {
        calls++
        return response()
    }
}