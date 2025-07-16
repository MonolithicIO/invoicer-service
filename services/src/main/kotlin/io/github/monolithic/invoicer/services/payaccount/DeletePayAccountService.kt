package io.github.monolithic.invoicer.services.payaccount

import io.github.monolithic.invoicer.services.company.GetUserCompanyDetailsService
import java.util.*
import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountTypeModel
import io.github.monolithic.invoicer.repository.PaymentAccountRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.conflictError
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface DeletePayAccountService {
    /**
     * * Deletes a payment account for a company. Note only intermediary accounts can be deleted.
     * Trying to delete a non-intermediary account will result in an error.
     *  @param companyId The ID of the company.
     *  @param userId The ID of the user performing the deletion.
     *  @param accountId The ID of the payment account to be deleted.
     *  @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError] if the user does not exist.
     *  @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError] if the company does not exist.
     *  @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError]
     *  if the user does not have access to the company.
     *  @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError]
     *  if the payment account does not exist.
     *  @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError]
     *  if the payment account is not an intermediary account.
     **/
    suspend fun delete(
        companyId: UUID,
        userId: UUID,
        accountId: UUID
    )
}

internal class DeletePayAccountServiceImpl(
    private val paymentAccountRepository: PaymentAccountRepository,
    private val getUserCompanyDetailsService: GetUserCompanyDetailsService,
) : DeletePayAccountService {

    override suspend fun delete(companyId: UUID, userId: UUID, accountId: UUID) {
        val company = getUserCompanyDetailsService.get(
            userId = userId,
            companyId = companyId
        )

        val account = paymentAccountRepository.getById(accountId) ?: notFoundError("Payment account not found")

        if (account.companyId != company.id) forbiddenError()

        if (account.type == PaymentAccountTypeModel.Primary) conflictError("Cannot delete primary payment account")

        paymentAccountRepository.delete(accountId)
    }
}
