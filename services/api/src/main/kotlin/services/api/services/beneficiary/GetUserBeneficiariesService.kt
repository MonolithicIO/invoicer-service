package services.api.services.beneficiary

import services.api.model.beneficiary.BeneficiaryModel
import services.api.repository.BeneficiaryRepository
import java.util.UUID

interface GetUserBeneficiariesService {
    suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel>
}

internal class GetUserBeneficiariesServiceImpl(
    private val repository: BeneficiaryRepository
) : GetUserBeneficiariesService {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel> {
        return repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )
    }
}