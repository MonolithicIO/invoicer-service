package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlinx.coroutines.test.runTest

class GetUserCompanyDetailsServiceImplTest {

    private lateinit var companyRepository: UserCompanyRepository
    private lateinit var getUserByIdService: GetUserByIdService
    private lateinit var service: GetUserCompanyDetailsServiceImpl

    private val userId = UUID.randomUUID()
    private val companyId = UUID.randomUUID()

    @BeforeTest
    fun setup() {
        companyRepository = mockk()
        getUserByIdService = mockk()
        service = GetUserCompanyDetailsServiceImpl(companyRepository, getUserByIdService)
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `get returns company when user owns company`() = runTest {
        val user = mockk<UserModel>()
        every { user.id } returns userId

        val companyUser = mockk<UserModel>()
        every { companyUser.id } returns userId

        val company = mockk<CompanyDetailsModel>()
        every { company.user } returns companyUser

        coEvery { getUserByIdService.get(userId) } returns user
        coEvery { companyRepository.getCompanyDetails(companyId) } returns company

        val result = service.get(userId, companyId)
        assertSame(company, result)
    }

    @Test
    fun `get throws when company not found`() {
        val user = mockk<UserModel>()
        every { user.id } returns userId

        coEvery { getUserByIdService.get(userId) } returns user
        coEvery { companyRepository.getCompanyDetails(companyId) } returns null

        assertFailsWith<Throwable> {
            runTest {
                service.get(userId, companyId)
            }
        }
    }

    @Test
    fun `get throws when user not found`() {
        coEvery { getUserByIdService.get(userId) } throws RuntimeException("user not found")
        // repository shouldn't be called but stub to be safe
        coEvery { companyRepository.getCompanyDetails(companyId) } returns null

        assertFailsWith<RuntimeException> {
            runTest {
                service.get(userId, companyId)
            }
        }
    }

    @Test
    fun `get throws when user does not own company`() = runTest {
        val user = mockk<UserModel>()
        every { user.id } returns userId

        val otherUser = mockk<UserModel>()
        every { otherUser.id } returns UUID.randomUUID()

        val company = mockk<CompanyDetailsModel>()
        every { company.user } returns otherUser

        coEvery { getUserByIdService.get(userId) } returns user
        coEvery { companyRepository.getCompanyDetails(companyId) } returns company

        assertFailsWith<Throwable> {
            service.get(userId, companyId)
        }
    }
}
