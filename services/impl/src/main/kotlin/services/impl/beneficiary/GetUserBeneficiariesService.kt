package services.impl.beneficiary

import models.beneficiary.BeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetUserBeneficiariesService
import java.util.*

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