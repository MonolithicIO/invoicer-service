package services.impl.beneficiary

import foundation.validator.impl.IbanValidator
import foundation.validator.impl.SwiftValidator
import models.beneficiary.BeneficiaryModel
import models.beneficiary.PartialUpdateBeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.beneficiary.UpdateBeneficiaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import java.util.*

internal class UpdateBeneficiaryServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getBeneficiaryByIdService: GetBeneficiaryByIdService,
    private val checkBeneficiarySwiftAvailableService: CheckBeneficiarySwiftAvailableService,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : UpdateBeneficiaryService {

    override suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel {
        validateSwift(model.swift)
        validateIban(model.iban)
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

        val user = getUserByIdService.get(userId)

        val beneficiary = getBeneficiaryByIdService.get(
            beneficiaryId = beneficiaryId,
            userId = user.id.toString()
        )

        if (beneficiary.swift != model.swift) {
            if (checkBeneficiarySwiftAvailableService.execute(
                    swift = model.swift,
                    userId = user.id.toString(),
                )
            ) {
                httpError(
                    message = "Swift code: ${model.swift} is already in use by another beneficiary",
                    code = HttpCode.Conflict
                )
            }
        }

        return beneficiaryRepository.update(
            userId = UUID.fromString(userId),
            beneficiaryId = UUID.fromString(beneficiary.id),
            model = buildUpdateModel(
                originalModel = beneficiary,
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

    private fun validateSwift(swift: String) {
        if (swiftValidator.validate(swift).not()) {
            badRequestError("Invalid swift code: $swift")
        }
    }

    private fun validateIban(iban: String) {
        if (ibanValidator.validate(iban).not()) {
            badRequestError("Invalid iban code: $iban")
        }
    }

    private fun takeIfChanged(
        newValue: String,
        originalValue: String
    ): String? {
        return if (newValue != originalValue) {
            null
        } else {
            newValue
        }
    }

    private fun buildUpdateModel(
        originalModel: BeneficiaryModel,
        newModel: UpdateBeneficiaryModel
    ): PartialUpdateBeneficiaryModel {
        return PartialUpdateBeneficiaryModel(
            name = takeIfChanged(
                newValue = originalModel.name,
                originalValue = newModel.name
            ),
            iban = takeIfChanged(
                newValue = originalModel.iban,
                originalValue = newModel.iban
            ),
            swift = takeIfChanged(
                newValue = originalModel.swift,
                originalValue = newModel.swift
            ),
            bankName = takeIfChanged(
                newValue = originalModel.bankName,
                originalValue = newModel.bankName
            ),
            bankAddress = takeIfChanged(
                newValue = originalModel.bankAddress,
                originalValue = newModel.bankAddress
            )
        )
    }
}