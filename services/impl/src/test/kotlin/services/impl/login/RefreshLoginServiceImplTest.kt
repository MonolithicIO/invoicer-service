package services.impl.login

import foundation.authentication.api.FakeAuthTokenManager
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import models.login.RefreshTokenModel
import repository.test.repository.FakeRefreshTokenRepository
import services.test.refreshtoken.FakeStoreRefreshTokenService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class RefreshLoginServiceImplTest {

    private lateinit var service: RefreshLoginServiceImpl
    private lateinit var tokenManager: FakeAuthTokenManager
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var refreshTokenRepository: FakeRefreshTokenRepository
    private lateinit var storeRefreshTokenService: FakeStoreRefreshTokenService

    @BeforeTest
    fun setUp() {
        tokenManager = FakeAuthTokenManager()
        getUserByIdService = FakeGetUserByIdService()
        refreshTokenRepository = FakeRefreshTokenRepository()
        storeRefreshTokenService = FakeStoreRefreshTokenService()
        service = RefreshLoginServiceImpl(
            tokenManager = tokenManager,
            getUserByIdService = getUserByIdService,
            refreshTokenRepository = refreshTokenRepository,
            storeRefreshTokenService = storeRefreshTokenService
        )
    }

    @Test
    fun `when refresh token is invalid then should throw unauthorized error`() = runTest {
        // Given
        tokenManager.verify = null

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.UnAuthorized,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token is not found then should throw unauthorized error`() = runTest {
        // Given
        refreshTokenRepository.userToken = { null }

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.UnAuthorized,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token disabled found then should throw unauthorized error`() = runTest {
        // Given
        refreshTokenRepository.userToken = {
            refreshTokenModel.copy(
                enabled = false
            )
        }

        val result = assertFailsWith<HttpError> {
            service.refreshLogin("1234")
        }

        assertEquals(
            expected = HttpCode.UnAuthorized,
            actual = result.statusCode
        )
    }

    @Test
    fun `when refresh token succeeds then should invalidate current token`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }

        service.refreshLogin("1234")

        val (userId, token) = refreshTokenRepository.invalidateCallStack.first()

        assertEquals(
            expected = FakeGetUserByIdService.DEFAULT_RESPONSE.id.toString(),
            actual = userId
        )

        assertEquals(
            expected = token,
            actual = "1234"
        )
    }

    @Test
    fun `when refresh token succeeds then should store new refresh token`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }
        tokenManager.refreshToken = "9876"

        service.refreshLogin("1234")

        val (token, userId) = storeRefreshTokenService.callHistory.first()

        assertEquals(
            expected = "9876",
            actual = token
        )

        assertEquals(
            expected = FakeGetUserByIdService.DEFAULT_RESPONSE.id.toString(),
            actual = userId
        )
    }

    @Test
    fun `when refresh token succeeds then should return genereated tokens`() = runTest {
        // Given
        refreshTokenRepository.userToken = { refreshTokenModel }
        tokenManager.refreshToken = "8888"
        tokenManager.token = "9999"

        val result = service.refreshLogin("1234")

        assertEquals(
            expected = "9999",
            actual = result.accessToken
        )

        assertEquals(
            expected = "8888",
            actual = result.refreshToken
        )
    }

    companion object {
        val refreshTokenModel = RefreshTokenModel(
            userId = "123e4567-e89b-12d3-a456-426614174000",
            token = "sampleToken",
            createdAt = LocalDate.parse("2023-01-01"),
            updatedAt = LocalDate.parse("2023-01-02"),
            enabled = true
        )
    }
}