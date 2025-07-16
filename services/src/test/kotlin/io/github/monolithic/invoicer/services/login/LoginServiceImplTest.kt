package io.github.monolithic.invoicer.services.login

import io.github.monolithic.invoicer.foundation.password.fakes.FakePasswordEncryption
import io.github.monolithic.invoicer.foundation.authentication.fakes.FakeAuthTokenManager
import io.github.monolithic.invoicer.services.fakes.refreshtoken.FakeStoreRefreshTokenService
import io.github.monolithic.invoicer.services.fakes.user.FakeGetUserByEmailService
import io.github.monolithic.invoicer.utils.fakes.FakeEmailValidator
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.login.LoginModel
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError

class LoginServiceImplTest {

    private lateinit var service: LoginServiceImpl
    private lateinit var getUserByEmailService: FakeGetUserByEmailService
    private lateinit var emailValidator: FakeEmailValidator
    private lateinit var passwordEncryption: FakePasswordEncryption
    private lateinit var authTokenManager: FakeAuthTokenManager
    private lateinit var storeRefreshTokenService: FakeStoreRefreshTokenService

    @BeforeTest
    fun setUp() {
        emailValidator = FakeEmailValidator()
        getUserByEmailService = FakeGetUserByEmailService()
        passwordEncryption = FakePasswordEncryption()
        authTokenManager = FakeAuthTokenManager()
        storeRefreshTokenService = FakeStoreRefreshTokenService()

        service = LoginServiceImpl(
            getUserByEmailService = getUserByEmailService,
            emailValidator = emailValidator,
            passwordEncryption = passwordEncryption,
            authTokenManager = authTokenManager,
            storeRefreshTokenService = storeRefreshTokenService
        )
    }

    @Test
    fun `when email format is invalid then should throw bad request`() = runTest {
        // Given
        emailValidator.response = false
        val model = LoginModel(email = "invalid-email", password = "password")

        // When
        val result = assertFailsWith<HttpError> {
            service.login(model)
        }

        // Then
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = result.statusCode
        )
    }

    @Test
    fun `when email is not found then should throw not found error`() = runTest {
        // Given
        getUserByEmailService.response = { null }

        val model = LoginModel(email = "invalid-email", password = "password")

        // When
        val result = assertFailsWith<HttpError> {
            service.login(model)
        }

        // Then
        assertEquals(
            expected = HttpCode.NotFound,
            actual = result.statusCode
        )
    }

    @Test
    fun `when password dont match then should throw not found error`() = runTest {
        // Given
        passwordEncryption.compareResponse = { false }

        val model = LoginModel(email = "invalid-email", password = "password")

        // When
        val result = assertFailsWith<HttpError> {
            service.login(model)
        }

        // Then
        assertEquals(
            expected = HttpCode.NotFound,
            actual = result.statusCode
        )
    }

    @Test
    fun `when login succeeds then should store refresh token`() = runTest {
        // give
        authTokenManager.refreshToken = "9876"

        val model = LoginModel(email = "invalid-email", password = "password")

        // When
        service.login(model)

        // Then
        assertTrue {
            storeRefreshTokenService.callHistory.contains(
                Pair(
                    "9876",
                    FakeGetUserByEmailService.DEFAULT_RESPONSE.id
                )
            )
        }
    }

    @Test
    fun `when login succeeds then should return generated tokens`() = runTest {
        // give
        authTokenManager.refreshToken = "9876"
        authTokenManager.token = "1234"

        val model = LoginModel(email = "invalid-email", password = "password")

        // When
        val result = service.login(model)

        // Then
        assertEquals(
            expected = "1234",
            actual = result.accessToken
        )

        assertEquals(
            expected = "9876",
            actual = result.refreshToken
        )
    }

}
