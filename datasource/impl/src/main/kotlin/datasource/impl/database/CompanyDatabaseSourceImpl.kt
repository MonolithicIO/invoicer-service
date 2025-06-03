package datasource.impl.database

import datasource.api.database.CompanyDatabaseSource
import datasource.api.model.company.CreateCompanyData
import datasource.api.model.paymentaccount.PaymentAccountType
import datasource.impl.entities.CompanyAddressTable
import datasource.impl.entities.PaymentAccountTable
import datasource.impl.entities.UserCompanyTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

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
}