package services.impl.beneficiary

import utils.exceptions.http.HttpCode
import io.github.alaksion.invoicer.utils.validation.IbanValidator
import io.github.alaksion.invoicer.utils.validation.SwiftValidator
import models.beneficiary.CreateBeneficiaryModel
import repository.api.repository.BeneficiaryRepository
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import services.api.services.beneficiary.CreateBeneficiaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.httpError
import java.util.*

internal class CreateBeneficiaryServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val repository: BeneficiaryRepository,
    private val checkSwiftService: CheckBeneficiarySwiftAvailableService,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator,
) : CreateBeneficiaryService {

    override suspend fun create(model: CreateBeneficiaryModel, userId: UUID): String {

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

        val user = getUserByIdService.get(userId.toString())

        if (checkSwiftService.execute(model.swift, userId)) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpCode.Conflict
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