package io.github.monolithic.invoicer.services.customer

import io.github.monolithic.invoicer.utils.validation.EmailValidator
import io.github.monolithic.invoicer.models.customer.CreateCustomerModel
import java.util.*
import io.github.monolithic.invoicer.repository.CustomerRepository
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.conflictError
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface CreateCustomerService {
    suspend fun createCustomer(
        userId: UUID,
        data: CreateCustomerModel
    ): String
}

internal class CreateCustomerServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val userCompanyRepository: UserCompanyRepository,
    private val customerRepository: CustomerRepository,
    private val emailValidator: EmailValidator
) : CreateCustomerService {

    override suspend fun createCustomer(
        userId: UUID,
        data: CreateCustomerModel
    ): String {
        validateData(data)

        val user = getUserByIdService.get(userId)
        val company = userCompanyRepository.getCompanyById(data.companyId)
            ?: notFoundError("Company not found")

        if (user.id != company.userId) forbiddenError()

        if (customerRepository.findByCompanyIdAndEmail(
                companyId = data.companyId,
                email = data.email
            ) != null
        ) {
            conflictError("Customer with this email already exists")
        }

        return customerRepository.createCustomer(
            data = data
        ).toString()
    }

    private fun validateData(data: CreateCustomerModel) {
        if (data.name.isBlank()) {
            badRequestError("Customer name cannot be empty")
        }

        if (emailValidator.validate(data.email).not()) {
            badRequestError("Invalid email format")
        }

        data.phone?.let {
            if (it.isBlank()) {
                badRequestError("Phone number cannot be empty")
            }
        }
    }
}
