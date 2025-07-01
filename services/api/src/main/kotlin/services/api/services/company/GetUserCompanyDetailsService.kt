package services.api.services.company

import models.company.CompanyDetailsModel
import java.util.*

interface GetUserCompanyDetailsService {

    /**
     * Searches for a company by user ID and company ID.
     * @exception [utils.exceptions.http.HttpError] if user is not found.
     * @exception [utils.exceptions.http.HttpError] if company is not found.
     * @exception [utils.exceptions.http.HttpError] if user has no access to the company.
     * */
    suspend fun get(
        userId: UUID,
        companyId: UUID
    ): CompanyDetailsModel
}