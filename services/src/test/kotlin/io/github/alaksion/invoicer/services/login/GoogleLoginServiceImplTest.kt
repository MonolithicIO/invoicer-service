package io.github.alaksion.invoicer.services.login

import foundation.password.fakes.FakePasswordEncryption
import io.github.alaksion.invoicer.foundation.authentication.fakes.FakeAuthTokenManager
import io.github.alaksion.invoicer.foundation.authentication.fakes.FakeIdentityProvider
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProviderError
import io.github.alaksion.invoicer.foundation.authentication.provider.IdentityProviderResult
import io.github.alaksion.invoicer.services.fakes.refreshtoken.FakeStoreRefreshTokenService
import io.github.alaksion.invoicer.services.fakes.user.FakeGetUserByEmailService
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import models.fixtures.userModelFixture
import repository.fakes.FakeUserRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

class GoogleLoginServiceImplTest {
    private lateinit var service: GoogleLoginServiceImpl
    private lateinit var identityProvider: FakeIdentityProvider
    private lateinit var userRepository: FakeUserRepository
    private lateinit var authTokenManager: FakeAuthTokenManager
    private lateinit var storeRefreshTokenService: FakeStoreRefreshTokenService
    private lateinit var getUserByEmailService: FakeGetUserByEmailService
    private lateinit var passwordEncryption: FakePasswordEncryption

    @BeforeTest
    fun setUp() {
        identityProvider = FakeIdentityProvider()
        userRepository = FakeUserRepository()
        authTokenManager = FakeAuthTokenManager()
        storeRefreshTokenService = FakeStoreRefreshTokenService()
        getUserByEmailService = FakeGetUserByEmailService()
        passwordEncryption = FakePasswordEncryption()

        service = GoogleLoginServiceImpl(
            identityProvider = identityProvider,
            getUserByEmailServiceImpl = getUserByEmailService,
            userRepository = userRepository,
            authTokenManager = authTokenManager,
            storeRefreshTokenService = storeRefreshTokenService,
            passwordEncryption = passwordEncryption
        )
    }

    @Test
    fun `should throw error if identity provider fails to retrieve user`() = runTest {
        identityProvider.response = { IdentityProviderResult.Error(error = IdentityProviderError.UNKNOWN_ERROR) }

        val error = assertFailsWith<HttpError> {
            service.login("token")
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should create new user if provider e-mail is not in use`() = runTest {
        identityProvider.response = {
            IdentityProviderResult.Success(
                email = "user@gmail.com",
                providerUuid = "123"
            )
        }
        getUserByEmailService.response = { null }

        service.login("token")

        assertEquals(
            expected = 1,
            actual = userRepository.createUserCallStack.size
        )

        assertEquals(
            expected = "user@gmail.com",
            actual = userRepository.createUserCallStack.first().email
        )
    }

    @Test
    fun `should return auth tokens for new user`() = runTest {
        identityProvider.response = {
            IdentityProviderResult.Success(
                email = "user@gmail.com",
                providerUuid = "123"
            )
        }
        userRepository.createUserResponse =
            { "12345678-1234-5678-1234-123456789012" }
        getUserByEmailService.response = { null }
        authTokenManager.refreshToken = "refresh"
        authTokenManager.token = "access-token"

        val response = service.login("token")

        assertEquals(
            expected = "refresh",
            actual = response.refreshToken
        )

        assertEquals(
            expected = "access-token",
            actual = response.accessToken
        )

        assertEquals(
            expected = Pair("refresh", UUID.fromString("12345678-1234-5678-1234-123456789012")),
            actual = storeRefreshTokenService.callHistory.first()
        )
    }

    @Test
    fun `should throw error if provider google e-mail is already in use`() = runTest {
        identityProvider.response = {
            IdentityProviderResult.Success(
                email = "user@gmail.com",
                providerUuid = "123"
            )
        }
        getUserByEmailService.response = {
            userModelFixture.copy(
                email = "user@gmail.com",
                identityProviderUuid = null
            )
        }

        val error = assertFailsWith<HttpError> {
            service.login("123")
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }

    @Test
    fun `should return auth tokens for existing user`() = runTest {
        identityProvider.response = {
            IdentityProviderResult.Success(
                email = "user@gmail.com",
                providerUuid = "123"
            )
        }
        getUserByEmailService.response = {
            userModelFixture.copy(
                email = "user@gmail.com",
                identityProviderUuid = "123",
                id = UUID.fromString("12345678-1234-5678-1234-123456789012")
            )
        }
        authTokenManager.refreshToken = "refresh"
        authTokenManager.token = "access-token"


        val response = service.login("123")


        assertEquals(
            expected = "refresh",
            actual = response.refreshToken
        )

        assertEquals(
            expected = "access-token",
            actual = response.accessToken
        )

        assertEquals(
            expected = Pair("refresh", UUID.fromString("12345678-1234-5678-1234-123456789012")),
            actual = storeRefreshTokenService.callHistory.first()
        )
    }
}
