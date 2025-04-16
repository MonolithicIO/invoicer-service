package services.impl.beneficiary

import io.github.alaksion.invoicer.utils.validation.IbanValidator
import io.github.alaksion.invoicer.utils.validation.SwiftValidator
import models.beneficiary.BeneficiaryModel
import models.beneficiary.PartialUpdateBeneficiaryModel
import models.beneficiary.UpdateBeneficiaryModel
import repository.BeneficiaryRepository
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.beneficiary.UpdateBeneficiaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.badRequestError
import utils.exceptions.http.httpError
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
        userId: UUID,
        beneficiaryId: UUID
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
            userId = user.id
        )

        if (beneficiary.swift != model.swift) {
            if (checkBeneficiarySwiftAvailableService.execute(
                    swift = model.swift,
                    userId = user.id,
                )
            ) {
                httpError(
                    message = "Swift code: ${model.swift} is already in use by another beneficiary",
                    code = HttpCode.Conflict
                )
            }
        }

        return beneficiaryRepository.update(
            userId = userId,
            beneficiaryId = beneficiary.id,
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
        return if (newValue == originalValue) {
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
}