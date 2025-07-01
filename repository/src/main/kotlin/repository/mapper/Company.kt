package repository.mapper

import models.company.CompanyAddressModel
import models.company.CompanyDetailsModel
import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.PaymentAccountTypeModel
import repository.entities.UserCompanyEntity

internal fun UserCompanyEntity.toCompanyDetails(): CompanyDetailsModel {
    return CompanyDetailsModel(
        id = id.value,
        name = name,
        document = document,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        address = address.first().let {
            CompanyAddressModel(
                addressLine1 = it.addressLine1,
                addressLine2 = it.addressLine2,
                city = it.city,
                state = it.state,
                postalCode = it.postalCode,
                countryCode = it.countryCode,
            )
        },
        userId = user.id.value,
        paymentAccount = payAccounts.first {
            it.type == "primary"
        }.let {
            PaymentAccountModel(
                iban = it.iban,
                swift = it.swift,
                bankName = it.bankName,
                bankAddress = it.bankAddress,
                type = PaymentAccountTypeModel.Primary,
                isDeleted = it.isDeleted,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                id = it.id.value,
                companyId = it.company.id.value
            )
        },
        intermediaryAccount = payAccounts.firstOrNull {
            it.type == "intermediary"
        }?.let {
            PaymentAccountModel(
                iban = it.iban,
                swift = it.swift,
                bankName = it.bankName,
                bankAddress = it.bankAddress,
                type = PaymentAccountTypeModel.Intermediary,
                isDeleted = it.isDeleted,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                id = it.id.value,
                companyId = it.company.id.value
            )
        },
        user = user.toModel()
    )
}