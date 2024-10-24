package services.impl.intermediary

import foundation.validator.api.SwiftValidator
import models.intermediary.IntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.UpdateIntermediaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.httpError
import java.util.*

internal class UpdateIntermediaryServiceImpl(
    private val getUserByIdUseCase: GetUserByIdService,
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val checkIntermediarySwiftAlreadyUsedService: CheckIntermediarySwiftAvailableService,
    private val intermediaryRepository: IntermediaryRepository,
    private val swiftValidator: SwiftValidator
) : UpdateIntermediaryService {

    override suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: String,
        intermediaryId: String
    ): IntermediaryModel {
        if (swiftValidator.validate(model.swift).not()) {
            httpError("Invalid swift code: ${model.swift}", HttpCode.BadRequest)
        }

        getUserByIdUseCase.get(userId)

        getIntermediaryByIdService.get(
            intermediaryId = intermediaryId,
            userId = userId
        )

        if (checkIntermediarySwiftAlreadyUsedService.execute(
                swift = model.swift,
                userId = userId
            )
        ) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpCode.Conflict
            )
        }

        return intermediaryRepository.update(
            userId = UUID.fromString(userId),
            intermediaryId = UUID.fromString(intermediaryId),
            model = model
        )
    }

}