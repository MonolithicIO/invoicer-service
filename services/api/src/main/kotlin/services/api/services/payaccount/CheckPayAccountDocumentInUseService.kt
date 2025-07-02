package services.api.services.payaccount

interface CheckPayAccountDocumentInUseService {

    suspend fun checkSwiftInUse(swift: String): Boolean

    suspend fun checkIbanInUse(iban: String): Boolean
}
