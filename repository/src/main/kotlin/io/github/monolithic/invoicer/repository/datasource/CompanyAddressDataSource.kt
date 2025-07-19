package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.models.company.UpdateCompanyAddressModel
import io.github.monolithic.invoicer.repository.entities.CompanyAddressTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

internal interface CompanyAddressDataSource {
    suspend fun updateCompanyAddress(
        model: UpdateCompanyAddressModel
    )
}

internal class CompanyAddressDataSourceImpl(
    private val clock: Clock
) : CompanyAddressDataSource {
    override suspend fun updateCompanyAddress(
        model: UpdateCompanyAddressModel
    ) {
        newSuspendedTransaction {
            CompanyAddressTable.update(
                where = {
                    CompanyAddressTable.company eq model.companyId
                }
            ) {
                it[CompanyAddressTable.addressLine1] = model.addressLine
                it[CompanyAddressTable.addressLine2] = model.addressLine2
                it[CompanyAddressTable.city] = model.city
                it[CompanyAddressTable.postalCode] = model.postalCode
                it[CompanyAddressTable.state] = model.state
                // No country code update in the model, so we leave it as is.
//                it[CompanyAddressTable.countryCode] = TODO
                it[CompanyAddressTable.updatedAt] = clock.now()
            }
        }
    }
}