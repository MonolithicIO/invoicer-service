package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.models.company.CreateCompanyAddressModel
import io.github.monolithic.invoicer.models.company.CreateCompanyModel
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.payaccount.CheckPayAccountDocumentInUseService
import io.github.monolithic.invoicer.utils.validation.CountryCodeValidator
import io.github.monolithic.invoicer.utils.validation.IbanValidator
import io.github.monolithic.invoicer.utils.validation.SwiftValidator
import java.util.*

interface CreateCompanyService {
    suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String
}

internal class CreateCompanyServiceImpl(
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator,
    private val countryCodeValidator: CountryCodeValidator,
    private val checkPayAccountDocumentService: CheckPayAccountDocumentInUseService,
    private val userCompanyRepository: UserCompanyRepository
) : CreateCompanyService {

    override suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID
    ): String {
        checkAccountDocumentConflict(
            primarySwift = data.paymentAccount.swift,
            intermediarySwift = data.intermediaryAccount?.swift,
            primaryIban = data.paymentAccount.iban,
            intermediaryIban = data.intermediaryAccount?.iban
        )

        validateSwiftCodes(
            primarySwift = data.paymentAccount.swift,
            intermediarySwift = data.intermediaryAccount?.swift
        )

        validateIbanCodes(
            primaryIban = data.paymentAccount.iban,
            intermediaryIban = data.intermediaryAccount?.iban
        )

        validateAddress(data.address)

        validateCompanyIdentity(
            document = data.document,
            name = data.name
        )

        return userCompanyRepository.createCompany(
            data = data,
            userId = userId
        )
    }

    private suspend fun validateIbanCodes(
        primaryIban: String,
        intermediaryIban: String?
    ) {
        if (!ibanValidator.validate(primaryIban)) badRequestError("Invalid IBAN for payment account: $primaryIban")
        if (checkPayAccountDocumentService.checkIbanInUse(primaryIban)) {
            badRequestError("IBAN $primaryIban for primary account is already in use.")
        }

        intermediaryIban?.let {
            if (!ibanValidator.validate(it)) badRequestError("Invalid IBAN for intermediary account: $intermediaryIban")
            if (checkPayAccountDocumentService.checkIbanInUse(it)) {
                badRequestError("IBAN $it for intermediary account is already in use.")
            }
        }
    }

    private suspend fun validateSwiftCodes(
        primarySwift: String,
        intermediarySwift: String?
    ) {
        if (!swiftValidator.validate(primarySwift))
            badRequestError("Invalid SWIFT code for payment account: $primarySwift")

        if (checkPayAccountDocumentService.checkSwiftInUse(primarySwift)) {
            badRequestError("SWIFT code $primarySwift for primary account is already in use.")
        }

        intermediarySwift?.let {
            if (!swiftValidator.validate(it))
                badRequestError("Invalid SWIFT code for intermediary account: $intermediarySwift")

            if (checkPayAccountDocumentService.checkSwiftInUse(it)) {
                badRequestError("SWIFT code $it for intermediary account is already in use.")
            }

        }
    }

    private fun checkAccountDocumentConflict(
        primarySwift: String,
        intermediarySwift: String? = null,
        primaryIban: String,
        intermediaryIban: String? = null
    ) {
        if (primaryIban == intermediaryIban) {
            badRequestError("Primary and intermediary IBANs cannot be the same: $primaryIban")
        }

        if (primarySwift == intermediarySwift) {
            badRequestError("Primary and intermediary SWIFT codes cannot be the same: $primarySwift")
        }
    }

    private fun validateAddress(address: CreateCompanyAddressModel) {
        if (address.addressLine1.isBlank()) {
            badRequestError("Address line 1 cannot be empty.")
        }

        if (address.city.isBlank()) {
            badRequestError("City cannot be empty.")
        }

        if (address.state.isBlank()) {
            badRequestError("State cannot be empty.")
        }

        if (address.postalCode.isBlank()) {
            badRequestError("Postal code cannot be empty.")
        }

        if (countryCodeValidator.validate(address.countryCode).not()) {
            badRequestError("Country code must be a 3-letter ISO 3166-1 alpha-3 code")
        }
    }

    private fun validateCompanyIdentity(
        document: String,
        name: String
    ) {
        if (document.isBlank()) {
            badRequestError("Document cannot be empty.")
        }

        if (name.isBlank()) {
            badRequestError("Company name cannot be empty.")
        }
    }
}
