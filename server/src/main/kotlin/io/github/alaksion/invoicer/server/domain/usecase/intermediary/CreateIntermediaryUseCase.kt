package io.github.alaksion.invoicer.server.domain.usecase.intermediary

import foundation.validator.api.SwiftValidator
import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
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
    private val repository: IntermediaryRepository,
    private val checkIntermediarySwiftAlreadyUsedUseCaseImpl: CheckIntermediarySwiftAvailableUseCase,
    private val swiftValidator: SwiftValidator
) : CreateIntermediaryUseCase {

    override suspend fun create(model: CreateIntermediaryModel, userId: String): String {
        if (swiftValidator.validate(model.swift).not()) {
            badRequestError("Invalid swift code: ${model.swift}")
        }

        val user = getUserByIdUseCase.get(userId)

        if (checkIntermediarySwiftAlreadyUsedUseCaseImpl.execute(model.swift, userId)) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another intermediary",
                code = HttpStatusCode.Conflict
            )
        }

        return repository.create(
            userId = user.id,
            model = model
        )
    }

}