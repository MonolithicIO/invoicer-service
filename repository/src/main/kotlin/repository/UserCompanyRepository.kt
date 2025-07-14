package repository

import java.util.*
import models.company.CompanyDetailsModel
import models.company.CompanyList
import models.company.CompanyListItem
import models.company.CompanyModel
import models.company.CreateCompanyModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.datasource.UserCompanyDataSource
import repository.entities.UserCompanyEntity
import repository.entities.UserCompanyTable

interface UserCompanyRepository {
    suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String

    suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList

    suspend fun getCompanyById(
        companyId: UUID
    ): CompanyModel?

    suspend fun getCompanyDetails(
        companyId: UUID
    ): CompanyDetailsModel?
}

internal class UserCompanyRepositoryImpl(
    private val datasource: UserCompanyDataSource
) : UserCompanyRepository {

    override suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String {
        return datasource.createCompany(
            data = data,
            userId = userId
        )
    }

    override suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList {
        return newSuspendedTransaction {
            val query = UserCompanyTable
                .selectAll()
                .where {
                    UserCompanyTable.user eq userId and (UserCompanyTable.isDeleted eq false)
                }
                .limit(n = limit, offset = (page * limit).toLong())

            val itemCount = query.count()

            val currentOffset = page * limit

            val nextPage = if (itemCount > currentOffset) {
                (itemCount - currentOffset) / limit
            } else {
                null
            }

            val result = UserCompanyEntity.wrapRows(query)
                .toList()
                .map {
                    CompanyListItem(
                        name = it.name,
                        document = it.document,
                        id = it.id.value
                    )
                }

            CompanyList(
                items = result,
                nextPage = nextPage,
                totalCount = itemCount
            )
        }
    }

    override suspend fun getCompanyById(companyId: UUID): CompanyModel? {
        return datasource.getCompanyById(
            companyId = companyId
        )
    }

    override suspend fun getCompanyDetails(companyId: UUID): CompanyDetailsModel? {
        return datasource.getCompanyDetails(companyId = companyId)
    }
}
