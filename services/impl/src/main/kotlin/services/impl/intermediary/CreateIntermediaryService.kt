package services.impl.intermediary

import foundation.validator.api.SwiftValidator
import models.intermediary.CreateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import services.api.services.intermediary.CreateIntermediaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError

internal class CreateIntermediaryServiceImpl(
    private val getUserByIdUseCase: GetUserByIdService,
    private val repository: IntermediaryRepository,
    private val checkIntermediarySwiftAlreadyUsedService: CheckIntermediarySwiftAvailableService,
    private val swiftValidator: SwiftValidator
) : CreateIntermediaryService {

    override suspend fun create(model: CreateIntermediaryModel, userId: String): String {
        if (swiftValidator.validate(model.swift).not()) {
            badRequestError("Invalid swift code: ${model.swift}")
        }

        val user = getUserByIdUseCase.get(userId)

        if (checkIntermediarySwiftAlreadyUsedService.execute(model.swift, userId)) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another intermediary",
                code = HttpCode.Conflict
            )
        }

        return repository.create(
            userId = user.id,
            model = model
        )
    }

}