package services.api.services.payaccount

interface CheckPayAccountDocumentInUseService {

    suspend fun findBySwift(swift: String): Boolean

    suspend fun findByIban(iban: String): Boolean
}