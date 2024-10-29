package services.test.intermediary

import services.api.services.intermediary.CheckIntermediarySwiftAvailableService

class FakeCheckIntermediarySwiftAvailableService : CheckIntermediarySwiftAvailableService {
    var response: suspend () -> Boolean = { true }


    override suspend fun execute(swift: String, userId: String): Boolean {
        return response()
    }
}