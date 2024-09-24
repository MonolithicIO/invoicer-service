package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import foundation.validator.api.IbanValidator
import foundation.validator.api.SwiftValidator
import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.UpdateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.HttpStatusCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import java.util.UUID

interface UpdateBeneficiaryUseCase {
    suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel
}

internal class UpdateBeneficiaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase,
    private val checkBeneficiarySwiftAvailableUseCase: CheckBeneficiarySwiftAvailableUseCase,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : UpdateBeneficiaryUseCase {

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

        val beneficiary = getBeneficiaryByIdUseCase.get(
            beneficiaryId = beneficiaryId,
            userId = user.id.toString()
        )

        if (checkBeneficiarySwiftAvailableUseCase.execute(
                swift = model.swift,
                userId = user.id.toString(),
            )
        ) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpStatusCode.Conflict
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