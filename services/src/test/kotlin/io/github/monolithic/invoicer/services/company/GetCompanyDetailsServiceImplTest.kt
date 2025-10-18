package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.models.fixtures.companyDetailsFixture
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest

class GetCompanyDetailsServiceImplTest {

    private lateinit var service: GetCompanyDetailsServiceImpl
    private lateinit var companyRepository: UserCompanyRepository

    @BeforeTest
    fun setUp() {
        companyRepository = mockk()
        service = GetCompanyDetailsServiceImpl(companyRepository)
    }

    @Test
    fun `get returns company details when company exists`() = runTest {
        // Arrange
        val companyId = UUID.randomUUID()
        coEvery { companyRepository.getCompanyDetails(companyId) } returns companyDetailsFixture

        // Act
        val result = service.get(companyId)

        // Assert
        assertEquals(companyDetailsFixture, result)
        coVerify { companyRepository.getCompanyDetails(companyId) }
    }

    @Test
    fun `get returns null when company does not exist`() = runTest {
        // Arrange
        val companyId = UUID.randomUUID()
        coEvery { companyRepository.getCompanyDetails(companyId) } returns null

        // Act
        val result = service.get(companyId)

        // Assert
        assertNull(result)
        coVerify { companyRepository.getCompanyDetails(companyId) }
    }
}
