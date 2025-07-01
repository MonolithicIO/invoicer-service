package services.impl.company

import io.github.alaksion.invoicer.utils.validation.IbanValidator
import io.github.alaksion.invoicer.utils.validation.SwiftValidator
import models.paymentaccount.UpdatePaymentAccountModel
import repository.PaymentAccountRepository
import services.api.services.company.GetUserCompanyDetailsService
import services.api.services.company.UpdatePayAccountService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError
import java.util.*

internal class UpdatePayAccountServiceImpl(
    private val paymentAccountRepository: PaymentAccountRepository,
    private val getUserCompanyDetailsService: GetUserCompanyDetailsService,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : UpdatePayAccountService {
    override suspend fun update(
        companyId: UUID,
        userId: UUID,
        model: UpdatePaymentAccountModel
    ) {
        validateModel(model)

        val company = getUserCompanyDetailsService.get(
            userId = userId,
            companyId = companyId
        )

        val account = paymentAccountRepository.getById(model.id) ?: notFoundError("Payment account not found")

        if (account.companyId != company.id) forbiddenError()

        if (checkSwiftInUse(swift = model.swift, currentAccountId = account.id)) {
            badRequestError("SWIFT code is already in use")
        }

        if (checkIbanInUse(iban = model.iban, currentAccountId = account.id)) {
            badRequestError("IBAN code is already in use")
        }

        paymentAccountRepository.update(model)
    }

    private fun validateModel(
        model: UpdatePaymentAccountModel
    ) {
        if (swiftValidator.validate(model.swift).not()) badRequestError("Invalid SWIFT code format")
        if (ibanValidator.validate(model.iban).not()) badRequestError("Invalid IBAN code format")
        if (model.bankName.isBlank()) badRequestError("Bank name cannot be empty")
        if (model.bankAddress.isBlank()) badRequestError("Bank address cannot be empty")
    }

    private suspend fun checkSwiftInUse(
        swift: String,
        currentAccountId: UUID,
    ): Boolean {
        val existingSwift = paymentAccountRepository.getBySwift(swift)

        return existingSwift?.let {
            existingSwift.id != currentAccountId
        } ?: false
    }

    private suspend fun checkIbanInUse(
        iban: String,
        currentAccountId: UUID,
    ): Boolean {
        val existingIban = paymentAccountRepository.getByIban(iban)

        return existingIban?.let {
            existingIban.id != currentAccountId
        } ?: false
    }
}