package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.models.company.CompanyList
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

class GetCompaniesServiceImplTest {

    private lateinit var service: GetCompaniesServiceImpl
    private lateinit var repository: UserCompanyRepository
    private lateinit var getUserByIdService: GetUserByIdService

    @BeforeTest
    fun setUp() {
        repository = mockk()
        getUserByIdService = mockk()
        service = GetCompaniesServiceImpl(
            repository = repository,
            getUserByIdService = getUserByIdService
        )
    }

    @Test
    fun `get returns company list when user exists`() = runTest {
        val userId = UUID.randomUUID()
        val page = 0
        val limit = 10
        val expectedCompanyList = CompanyList(
            items = emptyList(),
            totalCount = 0,
            nextPage = null,
        )

        coEvery { getUserByIdService.get(userId) } returns mockk()
        coEvery {
            repository.getCompaniesByUserId(
                userId = userId,
                page = page,
                limit = limit
            )
        } returns expectedCompanyList

        val result = service.get(userId, page, limit)

        assertEquals(expectedCompanyList, result)
        coVerify { getUserByIdService.get(userId) }
        coVerify {
            repository.getCompaniesByUserId(
                userId = userId,
                page = page,
                limit = limit
            )
        }
    }

    @Test
    fun `get propagates error when user does not exist`() = runTest {
        val userId = UUID.randomUUID()
        val page = 0
        val limit = 10
        val expectedException = RuntimeException("User not found")

        coEvery { getUserByIdService.get(userId) } throws expectedException

        kotlin.test.assertFailsWith<RuntimeException> {
            service.get(userId, page, limit)
        }

        coVerify { getUserByIdService.get(userId) }
        coVerify(exactly = 0) {
            repository.getCompaniesByUserId(any(), any(), any())
        }
    }
}
