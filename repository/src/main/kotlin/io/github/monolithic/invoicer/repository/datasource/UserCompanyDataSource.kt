package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.CompanyAddressTable
import io.github.monolithic.invoicer.repository.entities.PaymentAccountTable
import io.github.monolithic.invoicer.repository.entities.PaymentAccountType
import io.github.monolithic.invoicer.repository.entities.UserCompanyEntity
import io.github.monolithic.invoicer.repository.entities.UserCompanyTable
import io.github.monolithic.invoicer.repository.mapper.toCompanyDetails
import java.util.UUID
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.models.company.CompanyList
import io.github.monolithic.invoicer.models.company.CompanyListItem
import io.github.monolithic.invoicer.models.company.CompanyModel
import io.github.monolithic.invoicer.models.company.CreateCompanyModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface UserCompanyDataSource {
    suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String

    suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList

    suspend fun getCompanyById(
        companyId: UUID
    ): CompanyModel?

    suspend fun getCompanyDetails(
        companyId: UUID
    ): CompanyDetailsModel?
}

internal class UserCompanyDataSourceImpl(
    private val clock: Clock
) : UserCompanyDataSource {

    override suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String {
        return newSuspendedTransaction {
            val companyId = UserCompanyTable.insertAndGetId { entry ->
                entry[name] = data.name
                entry[document] = data.document
                entry[isDeleted] = false
                entry[createdAt] = clock.now()
                entry[updatedAt] = clock.now()
                entry[user] = userId
            }

            CompanyAddressTable.insert { addressEntry ->
                addressEntry[addressLine1] = data.address.addressLine1
                addressEntry[addressLine2] = data.address.addressLine2
                addressEntry[city] = data.address.city
                addressEntry[state] = data.address.state
                addressEntry[postalCode] = data.address.postalCode
                addressEntry[countryCode] = data.address.countryCode
                addressEntry[company] = companyId
                addressEntry[isDeleted] = false
                addressEntry[createdAt] = clock.now()
                addressEntry[updatedAt] = clock.now()
            }

            PaymentAccountTable.insert { primaryAccountEntry ->
                primaryAccountEntry[iban] = data.paymentAccount.iban
                primaryAccountEntry[swift] = data.paymentAccount.swift
                primaryAccountEntry[bankName] = data.paymentAccount.bankName
                primaryAccountEntry[bankAddress] = data.paymentAccount.bankAddress
                primaryAccountEntry[type] = PaymentAccountType.Primary.descriptor
                primaryAccountEntry[company] = companyId
                primaryAccountEntry[isDeleted] = false
                primaryAccountEntry[createdAt] = clock.now()
                primaryAccountEntry[updatedAt] = clock.now()
            }

            data.intermediaryAccount?.let { intermediaryAccount ->
                PaymentAccountTable.insert { intermediaryAccountEntry ->
                    intermediaryAccountEntry[iban] = intermediaryAccount.iban
                    intermediaryAccountEntry[swift] = intermediaryAccount.swift
                    intermediaryAccountEntry[bankName] = intermediaryAccount.bankName
                    intermediaryAccountEntry[bankAddress] = intermediaryAccount.bankAddress
                    intermediaryAccountEntry[type] = PaymentAccountType.Intermediary.descriptor
                    intermediaryAccountEntry[company] = companyId
                    intermediaryAccountEntry[isDeleted] = false
                    intermediaryAccountEntry[createdAt] = clock.now()
                    intermediaryAccountEntry[updatedAt] = clock.now()
                }
            }

            companyId.toString().also {
                commit()
            }
        }
    }

    override suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList {
        return newSuspendedTransaction {
            val query = UserCompanyTable
                .selectAll()
                .where {
                    UserCompanyTable.user eq userId and (UserCompanyTable.isDeleted eq false)
                }
                .limit(n = limit, offset = (page * limit).toLong())

            val itemCount = query.count()

            val currentOffset = page * limit

            val nextPage = if (itemCount > currentOffset) {
                (itemCount - currentOffset) / limit
            } else {
                null
            }

            val result = UserCompanyEntity.Companion.wrapRows(query)
                .toList()
                .map {
                    CompanyListItem(
                        name = it.name,
                        document = it.document,
                        id = it.id.value
                    )
                }

            CompanyList(
                items = result,
                nextPage = nextPage,
                totalCount = itemCount
            )
        }
    }

    override suspend fun getCompanyById(companyId: UUID): CompanyModel? {
        return newSuspendedTransaction {
            UserCompanyEntity.Companion.findById(companyId)?.let {
                CompanyModel(
                    name = it.name,
                    document = it.document,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    isDeleted = it.isDeleted,
                    id = it.id.value,
                    userId = it.user.id.value
                )
            }
        }
    }

    override suspend fun getCompanyDetails(companyId: UUID): CompanyDetailsModel? {
        return newSuspendedTransaction {
            UserCompanyEntity.Companion.findById(companyId)?.toCompanyDetails()
        }
    }
}
