package services.api.services.payaccount

import java.util.*

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
