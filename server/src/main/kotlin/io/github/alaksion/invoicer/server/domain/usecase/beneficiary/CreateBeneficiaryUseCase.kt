package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.github.alaksion.invoicer.server.validation.validateSwiftCode
import io.ktor.http.HttpStatusCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError

interface CreateBeneficiaryUseCase {
    suspend fun create(
        model: CreateBeneficiaryModel,
        userId: String,
    ): String
}

internal class CreateBeneficiaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val repository: BeneficiaryRepository
) : CreateBeneficiaryUseCase {

    override suspend fun create(model: CreateBeneficiaryModel, userId: String): String {
        if (validateSwiftCode(model.swift).not()) {
            badRequestError("Invalid swift code: ${model.swift}")
        }

        val user = getUserByIdUseCase.get(userId)

        repository.getBySwift(user.id, model.swift)
            ?: httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpStatusCode.Conflict
            )

        return repository.create(
            userId = user.id,
            model = model
        )
    }

}