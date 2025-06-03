package repository

import datasource.api.database.CompanyDatabaseSource
import datasource.api.model.company.CreateCompanyAddressData
import datasource.api.model.company.CreateCompanyData
import datasource.api.model.company.CreateCompanyPaymentAccountData
import models.company.CompanyList
import models.company.CreateCompanyModel
import java.util.*

interface UserCompanyRepository {
    suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String

    suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList
}

internal class UserCompanyRepositoryImpl(
    private val dataSource: CompanyDatabaseSource
) : UserCompanyRepository {

    override suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String {
        val result = dataSource.createCompany(
            data = CreateCompanyData(
                name = data.name,
                document = data.document,
                address = CreateCompanyAddressData(
                    addressLine1 = data.address.addressLine1,
                    addressLine2 = data.address.addressLine2,
                    city = data.address.city,
                    state = data.address.state,
                    postalCode = data.address.postalCode,
                    countryCode = data.address.countryCode
                ),
                paymentAccount = CreateCompanyPaymentAccountData(
                    iban = data.paymentAccount.iban,
                    swift = data.paymentAccount.swift,
                    bankName = data.paymentAccount.bankName,
                    bankAddress = data.paymentAccount.bankAddress
                ),
                intermediaryAccount = data.intermediaryAccount?.let {
                    CreateCompanyPaymentAccountData(
                        iban = it.iban,
                        swift = it.swift,
                        bankName = it.bankName,
                        bankAddress = it.bankAddress
                    )
                },
                userId = userId
            )
        )
        return result
    }

    override suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList {
        return dataSource.getUserCompanies(
            userId = userId,
            page = page,
            limit = limit
        )
    }
}