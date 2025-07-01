package services.api.services.company

import models.paymentaccount.UpdatePaymentAccountModel
import java.util.*

interface UpdatePayAccountService {
    /***
     * Updates a payment account for a company.
     * @exception [utils.exceptions.http.HttpError] if the user does not exist.
     * @exception [utils.exceptions.http.HttpError] if the company does not exist.
     * @exception [utils.exceptions.http.HttpError] if the user does not have access to the company.
     * @exception [utils.exceptions.http.HttpError] if swift or iban codes are invalid.
     * @exception [utils.exceptions.http.HttpError] if swift or iban codes are already in use.
     * */
    suspend fun update(
        companyId: UUID,
        userId: UUID,
        model: UpdatePaymentAccountModel
    )
}