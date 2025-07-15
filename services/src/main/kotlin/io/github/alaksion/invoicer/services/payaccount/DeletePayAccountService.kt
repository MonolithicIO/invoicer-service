package io.github.alaksion.invoicer.services.payaccount

import io.github.alaksion.invoicer.services.company.GetUserCompanyDetailsService
import java.util.*
import models.paymentaccount.PaymentAccountTypeModel
import repository.PaymentAccountRepository
import utils.exceptions.http.conflictError
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

interface DeletePayAccountService {
    /**
     * * Deletes a payment account for a company. Note only intermediary accounts can be deleted.
     * Trying to delete a non-intermediary account will result in an error.
     *  @param companyId The ID of the company.
     *  @param userId The ID of the user performing the deletion.
     *  @param accountId The ID of the payment account to be deleted.
     *  @exception [utils.exceptions.http.HttpError] if the user does not exist.
     *  @exception [utils.exceptions.http.HttpError] if the company does not exist.
     *  @exception [utils.exceptions.http.HttpError] if the user does not have access to the company.
     *  @exception [utils.exceptions.http.HttpError] if the payment account does not exist.
     *  @exception [utils.exceptions.http.HttpError] if the payment account is not an intermediary account.
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
