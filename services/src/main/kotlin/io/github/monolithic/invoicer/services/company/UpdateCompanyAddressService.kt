package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError
import io.github.monolithic.invoicer.models.company.UpdateCompanyAddressModel
import io.github.monolithic.invoicer.repository.CompanyAddressRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import java.util.*

interface UpdateCompanyAddressService {
    suspend fun updateCompanyAddress(
        model: UpdateCompanyAddressModel,
        userId: UUID
    )
}

internal class UpdateCompanyAddressServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getCompanyByIdService: GetCompanyDetailsService,
    private val companyAddressRepository: CompanyAddressRepository
) : UpdateCompanyAddressService {
    override suspend fun updateCompanyAddress(
        model: UpdateCompanyAddressModel,
        userId: UUID
    ) {
        validateModel(model)
        val user = getUserByIdService.get(userId)
        val company = getCompanyByIdService.get(model.companyId) ?: notFoundError("Company not found")

        if (company.user.id != user.id) forbiddenError()

        companyAddressRepository.updateAddress(model)
    }

    private fun validateModel(model: UpdateCompanyAddressModel) {
        if (model.addressLine.isBlank()) badRequestError("Address line cannot be blank")

        model.addressLine2?.let { addressLine2 ->
            if (addressLine2.isBlank()) badRequestError("Address line 2 cannot be blank")
        }

        if (model.city.isBlank()) badRequestError("City cannot be blank")

        if (model.state.isBlank()) badRequestError("State cannot be blank")

        if (model.postalCode.isEmpty()) badRequestError("Postal code cannot be empty")
    }

}
