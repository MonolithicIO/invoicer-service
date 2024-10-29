package services.impl.intermediary

import foundation.validator.api.IbanValidator
import foundation.validator.api.SwiftValidator
import models.intermediary.IntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.intermediary.UpdateIntermediaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import java.util.*

internal class UpdateIntermediaryServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val checkIntermediarySwiftAlreadyUsedService: CheckIntermediarySwiftAvailableService,
    private val intermediaryRepository: IntermediaryRepository,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : UpdateIntermediaryService {

    override suspend fun execute(
        model: UpdateIntermediaryModel,
        userId: String,
        intermediaryId: String
    ): IntermediaryModel {

        validateString(
            value = model.name,
            fieldName = "Name"
        )
        validateString(
            value = model.bankName,
            fieldName = "Bank name"
        )
        validateString(
            value = model.bankAddress,
            fieldName = "Bank address"
        )

        if (ibanValidator.validate(model.iban).not()) {
            httpError("Invalid IBAN code: ${model.iban}", HttpCode.BadRequest)
        }

        if (swiftValidator.validate(model.swift).not()) {
            httpError("Invalid swift code: ${model.swift}", HttpCode.BadRequest)
        }

        getUserByIdService.get(userId)

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

    private fun validateString(
        value: String,
        fieldName: String
    ) {
        if (value.trim().isBlank()) {
            badRequestError("$fieldName cannot be blank")
        }
    }

}