package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import foundation.validator.api.IbanValidator
import foundation.validator.api.SwiftValidator
import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.HttpStatusCode
import utils.exceptions.badRequestError
import utils.exceptions.httpError

interface CreateBeneficiaryUseCase {
    suspend fun create(
        model: CreateBeneficiaryModel,
        userId: String,
    ): String
}

internal class CreateBeneficiaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val repository: BeneficiaryRepository,
    private val checkSwiftUseCase: CheckBeneficiarySwiftAvailableUseCase,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator,
) : CreateBeneficiaryUseCase {

    override suspend fun create(model: CreateBeneficiaryModel, userId: String): String {

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

        if (checkSwiftUseCase.execute(model.swift, userId)) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpStatusCode.Conflict
            )
        }

        return repository.create(
            userId = user.id,
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