package services.impl.intermediary

import io.github.alaksion.invoicer.utils.http.HttpCode
import io.github.alaksion.invoicer.utils.validation.IbanValidator
import io.github.alaksion.invoicer.utils.validation.SwiftValidator
import models.intermediary.IntermediaryModel
import models.intermediary.PartialUpdateIntermediaryModel
import models.intermediary.UpdateIntermediaryModel
import repository.api.repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.intermediary.UpdateIntermediaryService
import services.api.services.user.GetUserByIdService
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

        val intermediary = getIntermediaryByIdService.get(
            intermediaryId = intermediaryId,
            userId = userId
        )

        if (intermediary.swift != model.swift) {
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
        }

        return intermediaryRepository.update(
            userId = UUID.fromString(userId),
            intermediaryId = UUID.fromString(intermediaryId),
            model = buildUpdateModel(
                originalModel = intermediary,
                newModel = model
            )
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

    private fun buildUpdateModel(
        originalModel: IntermediaryModel,
        newModel: UpdateIntermediaryModel
    ): PartialUpdateIntermediaryModel {
        return PartialUpdateIntermediaryModel(
            name = takeIfChanged(
                newValue = newModel.name,
                originalValue = originalModel.name
            ),
            iban = takeIfChanged(
                newValue = newModel.iban,
                originalValue = originalModel.iban
            ),
            swift = takeIfChanged(
                newValue = newModel.swift,
                originalValue = originalModel.swift
            ),
            bankName = takeIfChanged(
                newValue = newModel.bankName,
                originalValue = originalModel.bankName
            ),
            bankAddress = takeIfChanged(
                newValue = newModel.bankAddress,
                originalValue = originalModel.bankAddress
            )
        )
    }

    private fun takeIfChanged(
        newValue: String,
        originalValue: String
    ): String? {
        return if (newValue == originalValue) {
            null
        } else {
            newValue
        }
    }

}