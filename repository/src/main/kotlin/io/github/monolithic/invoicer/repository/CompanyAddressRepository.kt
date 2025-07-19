package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.company.UpdateCompanyAddressModel
import io.github.monolithic.invoicer.repository.datasource.CompanyAddressDataSource

interface CompanyAddressRepository {
    suspend fun updateAddress(
        model: UpdateCompanyAddressModel
    )
}

internal class CompanyAddressRepositoryImpl(
    private val dataSource: CompanyAddressDataSource
) : CompanyAddressRepository {

    override suspend fun updateAddress(
        model: UpdateCompanyAddressModel
    ) {
        dataSource.updateCompanyAddress(model)
    }
}
