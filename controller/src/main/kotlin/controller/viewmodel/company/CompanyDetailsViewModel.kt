package controller.viewmodel.company

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.company.CompanyDetailsModel
import models.user.UserModel

@Serializable
internal data class CompanyDetailsViewModel(
    val name: String,
    val document: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val userId: String,
    val id: String,
    val address: CompanyDetailsAddressViewModel,
    val paymentAccount: CompanyDetailsPaymentViewModel,
    val intermediaryAccount: CompanyDetailsPaymentViewModel?,
    val user: UserModel
)

@Serializable
internal data class CompanyDetailsAddressViewModel(
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val countryCode: String,
)

@Serializable
internal data class CompanyDetailsPaymentViewModel(
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val type: String,
    val isDeleted: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant,
    val id: String,
)

internal fun CompanyDetailsModel.toViewModel(): CompanyDetailsViewModel {
    return CompanyDetailsViewModel(
        name = this.name,
        document = this.document,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        userId = this.userId.toString(),
        id = this.id.toString(),
        address = CompanyDetailsAddressViewModel(
            addressLine1 = this.address.addressLine1,
            addressLine2 = this.address.addressLine2,
            city = this.address.city,
            state = this.address.state,
            postalCode = this.address.postalCode,
            countryCode = this.address.countryCode
        ),
        paymentAccount = CompanyDetailsPaymentViewModel(
            iban = this.paymentAccount.iban,
            swift = this.paymentAccount.swift,
            bankName = this.paymentAccount.bankName,
            bankAddress = this.paymentAccount.bankAddress,
            type = this.paymentAccount.type.name,
            isDeleted = this.paymentAccount.isDeleted,
            createdAt = this.paymentAccount.createdAt,
            updatedAt = this.paymentAccount.updatedAt,
            id = this.paymentAccount.id.toString()
        ),
        intermediaryAccount = this.intermediaryAccount?.let {
            CompanyDetailsPaymentViewModel(
                iban = it.iban,
                swift = it.swift,
                bankName = it.bankName,
                bankAddress = it.bankAddress,
                type = it.type.name,
                isDeleted = it.isDeleted,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                id = it.id.toString()
            )
        },
        user = this.user
    )
}
