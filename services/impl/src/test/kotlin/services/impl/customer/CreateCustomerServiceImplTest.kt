package services.impl.customer

import io.github.alaksion.invoicer.utils.fakes.FakeEmailValidator
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.company.CompanyModel
import models.customer.CreateCustomerModel
import models.customer.CustomerModel
import repository.fakes.FakeCustomerRepository
import repository.fakes.FakeUserCompanyRepository
import services.api.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

class CreateCustomerServiceImplTest {

    private lateinit var service: CreateCustomerServiceImpl
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var userCompanyRepository: FakeUserCompanyRepository
    private lateinit var customerRepository: FakeCustomerRepository
    private lateinit var emailValidator: FakeEmailValidator

    @BeforeTest
    fun setUp() {
        getUserByIdService = FakeGetUserByIdService()
        userCompanyRepository = FakeUserCompanyRepository()
        customerRepository = FakeCustomerRepository()
        emailValidator = FakeEmailValidator()

        service = CreateCustomerServiceImpl(
            getUserByIdService = getUserByIdService,
            userCompanyRepository = userCompanyRepository,
            customerRepository = customerRepository,
            emailValidator = emailValidator
        )
    }

    @Test
    fun `should fail if customer name is empty`() = runTest {
        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                data = CreateCustomerModel(
                    name = "",
                    email = "awdawd@gmail.com",
                    phone = null,
                    companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
                )
            )
        }

        assertTrue { error.statusCode == HttpCode.BadRequest }

    }

    @Test
    fun `should fail if email is invalid`() = runTest {
        emailValidator.response = false

        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                data = CreateCustomerModel(
                    name = "adwda",
                    email = "awdadoiwadhoiawhd",
                    phone = null,
                    companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
                )
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should fail if phone is informed but empty`() = runTest {
        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                data = CreateCustomerModel(
                    name = "adwda",
                    email = "adwa@gmail.com",
                    phone = "",
                    companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
                )
            )
        }

        assertTrue { error.statusCode == HttpCode.BadRequest }
    }

    @Test
    fun `should fail if company does not exist`() = runTest {
        userCompanyRepository.getCompanyByIdResponse = { null }

        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                data = CreateCustomerModel(
                    name = "adwda",
                    email = "adwa@gmail.com",
                    phone = "",
                    companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
                )
            )
        }

        assertTrue { error.statusCode == HttpCode.BadRequest }
    }

    @Test
    fun `should fail if company does not belong to user`() = runTest {
        userCompanyRepository.getCompanyByIdResponse = { company }

        getUserByIdService.response = {
            FakeGetUserByIdService.DEFAULT_RESPONSE.copy(
                id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002")
            )
        }

        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                data = CreateCustomerModel(
                    name = "adwda",
                    email = "adwa@gmail.com",
                    phone = null,
                    companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")
                )
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }

    @Test
    fun `should fail if customer with same email already exists`() = runTest {
        userCompanyRepository.getCompanyByIdResponse = { company }

        getUserByIdService.response = {
            FakeGetUserByIdService.DEFAULT_RESPONSE.copy(
                id = USER_ID
            )
        }

        customerRepository.findByCompanyIdAndEmailResponse = { existingCustomerModel }

        val error = assertFailsWith<HttpError> {
            service.createCustomer(
                userId = USER_ID,
                data = CreateCustomerModel(
                    name = "adwda",
                    email = "awdwa@gmail.com",
                    phone = null,
                    companyId = company.id
                )
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }

    @Test
    fun `should successfully create customer`() = runTest {
        userCompanyRepository.getCompanyByIdResponse = { company }

        getUserByIdService.response = {
            FakeGetUserByIdService.DEFAULT_RESPONSE.copy(
                id = USER_ID
            )
        }

        customerRepository.createCustomerResponse = { UUID.randomUUID() }

        service.createCustomer(
            userId = USER_ID,
            data = CreateCustomerModel(
                name = "adwda",
                email = "awdwa@gmail.com",
                phone = null,
                companyId = company.id
            )
        )
    }


    companion object {
        val USER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")

        val company = CompanyModel(
            userId = USER_ID,
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            name = "Test Company",
            document = "123456789",
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
            isDeleted = false,
        )

        val existingCustomerModel = CustomerModel(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002"),
            name = "customer",
            email = "email@adwad.com",
            phone = null,
            companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
        )
    }
}
