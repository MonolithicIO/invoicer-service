package services.api.services.beneficiary

import foundation.validator.api.IbanValidator
import foundation.validator.api.SwiftValidator
import services.api.model.beneficiary.BeneficiaryModel
import services.api.model.beneficiary.UpdateBeneficiaryModel
import services.api.repository.BeneficiaryRepository
import services.api.services.user.GetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import java.util.UUID

interface UpdateBeneficiaryUseService {
    suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel
}

internal class UpdateBeneficiaryUseServiceImpl(
    private val getUserByIdUseCase: GetUserByIdService,
    private val getBeneficiaryByIdService: GetBeneficiaryByIdService,
    private val checkBeneficiarySwiftAvailableService: CheckBeneficiarySwiftAvailableService,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : UpdateBeneficiaryUseService {

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

        val user = getUserByIdUseCase.get(userId)

        val beneficiary = getBeneficiaryByIdService.get(
            beneficiaryId = beneficiaryId,
            userId = user.id.toString()
        )

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

        return beneficiaryRepository.update(
            userId = UUID.fromString(userId),
            beneficiaryId = UUID.fromString(beneficiary.id),
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
}