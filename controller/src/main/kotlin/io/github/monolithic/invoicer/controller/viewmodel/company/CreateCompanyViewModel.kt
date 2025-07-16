package io.github.monolithic.invoicer.controller.viewmodel.company

import io.github.monolithic.invoicer.controller.validation.requiredString
import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.company.CreateCompanyAddressModel
import io.github.monolithic.invoicer.models.company.CreateCompanyModel
import io.github.monolithic.invoicer.models.company.CreateCompanyPaymentAccountModel
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

@Serializable
internal data class CreateCompanyViewModel(
    val name: String? = null,
    val document: String? = null,
    val address: CreateCompanyAddressViewModel? = null,
    val paymentAccount: CreateCompanyPaymentAccountViewModel? = null,
    val intermediaryAccount: CreateCompanyPaymentAccountViewModel? = null,
)

@Serializable
internal data class CreateCompanyAddressViewModel(
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val countryCode: String? = null,
)

@Serializable
internal data class CreateCompanyPaymentAccountViewModel(
    val iban: String? = null,
    val swift: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null,
)

/***
 *  Converts [CreateCompanyViewModel] to [models.company.CreateCompanyModel]. Throws [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError]
 *  if any required field is missing.
 */
internal fun CreateCompanyViewModel.toModel(): CreateCompanyModel {
    return CreateCompanyModel(
        name = requiredString(name, "Company name is required"),
        document = requiredString(document, "Document is required"),
        address = address?.let {
            CreateCompanyAddressModel(
                addressLine1 = requiredString(it.addressLine1, "Company address line 1 is required"),
                addressLine2 = it.addressLine2,
                city = requiredString(it.city, "Company city is required"),
                state = requiredString(it.state, "Company state is required"),
                postalCode = requiredString(it.postalCode, "Company postal code is required"),
                countryCode = requiredString(it.countryCode, "Company country code is required")
            )
        } ?: badRequestError("Company address is required"),
        paymentAccount = paymentAccount?.let {
            CreateCompanyPaymentAccountModel(
                iban = requiredString(it.iban, "Payment account IBAN is required"),
                swift = requiredString(it.swift, "Payment account SWIFT is required"),
                bankName = requiredString(it.bankName, "Payment account bank name is required"),
                bankAddress = requiredString(it.bankAddress, "Payment account bank address is required")
            )
        } ?: badRequestError("Payment account is required"),
        intermediaryAccount = intermediaryAccount?.let {
            CreateCompanyPaymentAccountModel(
                iban = requiredString(it.iban, "Intermediary account IBAN is required"),
                swift = requiredString(it.swift, "Intermediary account SWIFT is required"),
                bankName = requiredString(it.bankName, "Intermediary account bank name is required"),
                bankAddress = requiredString(it.bankAddress, "Intermediary account bank address is required")
            )
        }
    )
}
