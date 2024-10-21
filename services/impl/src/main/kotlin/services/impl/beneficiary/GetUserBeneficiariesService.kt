package services.impl.beneficiary

import models.beneficiary.BeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.GetUserBeneficiariesService
import services.api.services.user.GetUserByIdService
import utils.exceptions.unauthorizedResourceError
import java.util.*

internal class GetUserBeneficiariesServiceImpl(
    private val repository: BeneficiaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
) : GetUserBeneficiariesService {

    override suspend fun execute(
        userId: String,
        page: Long,
        limit: Int,
    ): List<BeneficiaryModel> {
        getUserByIdUseCase.get(userId)

        val beneficiaries = repository.getAll(
            userId = UUID.fromString(userId),
            page = page,
            limit = limit
        )

        if (beneficiaries.any { beneficiary ->
                beneficiary.userId != userId
            }
        ) {
            unauthorizedResourceError()
        }

        return beneficiaries
    }
}