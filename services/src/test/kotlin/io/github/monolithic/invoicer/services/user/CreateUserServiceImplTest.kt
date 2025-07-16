package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.password.PasswordStrength
import io.github.monolithic.invoicer.foundation.password.fakes.FakePasswordEncryption
import io.github.monolithic.invoicer.foundation.password.fakes.FakePasswordValidator
import io.github.monolithic.invoicer.services.fakes.user.FakeGetUserByEmailService
import io.github.monolithic.invoicer.utils.fakes.FakeEmailValidator
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.fixtures.userModelFixture
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.repository.fakes.FakeUserRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError


class CreateUserServiceImplTest {

    private lateinit var service: CreateUserServiceImpl
    private lateinit var getUserByEmailService: FakeGetUserByEmailService
    private lateinit var passwordValidator: FakePasswordValidator
    private lateinit var repository: FakeUserRepository
    private lateinit var passwordEncryption: FakePasswordEncryption
    private lateinit var emailValidator: FakeEmailValidator

    @BeforeTest
    fun setUp() {
        getUserByEmailService = FakeGetUserByEmailService()
        passwordValidator = FakePasswordValidator()
        repository = FakeUserRepository()
        passwordEncryption = FakePasswordEncryption()
        emailValidator = FakeEmailValidator()

        service = CreateUserServiceImpl(
            getUserByEmailService = getUserByEmailService,
            passwordValidator = passwordValidator,
            repository = repository,
            passwordEncryption = passwordEncryption,
            emailValidator = emailValidator,
        )
    }

    @Test
    fun `should throw error if email dont match`() = runTest {
        val error = assertFailsWith<HttpError> {
            service.create(
                userModel = CreateUserModel(
                    email = "abc@gmail.com",
                    confirmEmail = "def@gmail.com",
                    password = "123"
                )
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "E-mails must mach",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if e-mail validation fails`() = runTest {
        emailValidator.response = false

        val error = assertFailsWith<HttpError> {
            service.create(
                userModel = CreateUserModel(
                    email = "abc@gmail.com",
                    confirmEmail = "abc@gmail.com",
                    password = "123"
                )
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "invalid email format: abc@gmail.com",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if e-mail is in use`() = runTest {
        getUserByEmailService.response = { userModelFixture }

        val error = assertFailsWith<HttpError> {
            service.create(
                userModel = CreateUserModel(
                    email = "abc@gmail.com",
                    confirmEmail = "abc@gmail.com",
                    password = "123"
                )
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )

        assertEquals(
            expected = "E-mail abc@gmail.com already in use",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if password fails strength check`() = runTest {
        passwordValidator.response = { PasswordStrength.WEAK("weak") }
        getUserByEmailService.response = { null }

        val error = assertFailsWith<HttpError> {
            service.create(
                userModel = CreateUserModel(
                    email = "abc@gmail.com",
                    confirmEmail = "abc@gmail.com",
                    password = "123"
                )
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "weak",
            actual = error.message
        )
    }

    @Test
    fun `should create user successfully`() = runTest {
        repository.createUserResponse = { "123" }
        getUserByEmailService.response = { null }

        val result = service.create(
            userModel = CreateUserModel(
                email = "abc@gmail.com",
                confirmEmail = "abc@gmail.com",
                password = "123"
            )
        )

        assertEquals(
            expected = "123",
            actual = result
        )

        assertEquals(
            expected = 1,
            actual = repository.createUserCallStack.size
        )
    }

    @Test
    fun `should create user with encrypted password`() = runTest {
        passwordEncryption.encryptResponse = { "encrypted" }
        getUserByEmailService.response = { null }

        service.create(
            userModel = CreateUserModel(
                email = "abc@gmail.com",
                confirmEmail = "abc@gmail.com",
                password = "123"
            )
        )

        assertEquals(
            expected = "encrypted",
            actual = repository.createUserCallStack.first().password
        )

        assertEquals(
            expected = 1,
            actual = repository.createUserCallStack.size
        )
    }
}
