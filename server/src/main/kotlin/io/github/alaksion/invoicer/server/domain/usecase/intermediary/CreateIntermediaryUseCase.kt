package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.github.alaksion.invoicer.server.validation.validateSwiftCode
import io.ktor.http.HttpStatusCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError

interface CreateIntermediaryUseCase {
    suspend fun create(
        model: CreateIntermediaryModel,
        userId: String,
    ): String
}

internal class CreateIntermediaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val repository: IntermediaryRepository
) : CreateIntermediaryUseCase {

    override suspend fun create(model: CreateIntermediaryModel, userId: String): String {
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