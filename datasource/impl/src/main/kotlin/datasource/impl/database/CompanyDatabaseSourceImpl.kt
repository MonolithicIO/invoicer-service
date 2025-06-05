package datasource.impl.database

import datasource.api.database.CompanyDatabaseSource
import datasource.api.model.company.CreateCompanyData
import datasource.api.model.paymentaccount.PaymentAccountType
import datasource.impl.entities.CompanyAddressTable
import datasource.impl.entities.PaymentAccountTable
import datasource.impl.entities.UserCompanyEntity
import datasource.impl.entities.UserCompanyTable
import kotlinx.datetime.Clock
import models.company.CompanyList
import models.company.CompanyListItem
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

internal class CompanyDatabaseSourceImpl(
    private val clock: Clock
) : CompanyDatabaseSource {

    override suspend fun createCompany(data: CreateCompanyData): String {
        return newSuspendedTransaction {
            val companyId = UserCompanyTable.insertAndGetId { entry ->
                entry[name] = data.name
                entry[document] = data.document
                entry[isDeleted] = false
                entry[createdAt] = clock.now()
                entry[updatedAt] = clock.now()
                entry[user] = data.userId
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

    override suspend fun getUserCompanies(
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

            val result = UserCompanyEntity.wrapRows(query)
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
}