package services.impl.payaccount

import models.paymentaccount.PaymentAccountTypeModel
import repository.PaymentAccountRepository
import services.api.services.company.GetUserCompanyDetailsService
import services.api.services.payaccount.DeletePayAccountService
import utils.exceptions.http.conflictError
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError
import java.util.*

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