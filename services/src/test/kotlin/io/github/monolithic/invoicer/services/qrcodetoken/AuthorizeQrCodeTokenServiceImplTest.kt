package io.github.monolithic.invoicer.services.qrcodetoken

import io.github.monolithic.invoicer.foundation.authentication.fakes.FakeAuthTokenManager
import io.github.monolithic.invoicer.services.fakes.refreshtoken.FakeStoreRefreshTokenService
import io.github.monolithic.invoicer.services.fakes.user.FakeGetUserByIdService
import io.github.monolithic.invoicer.utils.fakes.FakeClock
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.fixtures.qrCodeTokenModelFixture
import io.github.monolithic.invoicer.models.qrcodetoken.AuthorizedQrCodeToken
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenStatusModel
import io.github.monolithic.invoicer.repository.fakes.FakeQrCodeTokenRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError

class AuthorizeQrCodeTokenServiceImplTest {
    private lateinit var service: AuthorizeQrCodeTokenServiceImpl
    private lateinit var storeTokenService: FakeStoreRefreshTokenService
    private lateinit var clock: FakeClock
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var authTokenManager: FakeAuthTokenManager
    private lateinit var qrCodeTokenRepository: FakeQrCodeTokenRepository


    @BeforeTest
    fun setUp() {
        storeTokenService = FakeStoreRefreshTokenService()
        clock = FakeClock()
        getUserByIdService = FakeGetUserByIdService()
        authTokenManager = FakeAuthTokenManager()
        qrCodeTokenRepository = FakeQrCodeTokenRepository()

        service = AuthorizeQrCodeTokenServiceImpl(
            authTokenManager = authTokenManager,
            storeRefreshTokenService = storeTokenService,
            qrCodeTokenRepository = qrCodeTokenRepository,
            clock = clock,
            getUserByIdService = getUserByIdService,
        )
    }

    @Test
    fun `should throw error if qrtoken does not exist`() = runTest {
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = { null }

        val error = assertFailsWith<HttpError> {
            service.consume(
                contentId = "contentId",
                userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
            )
        }

        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Qr Code Token not found",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if qrtoken is already consumed`() = runTest {
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = {
            qrCodeTokenModelFixture.copy(
                status = QrCodeTokenStatusModel.CONSUMED
            )
        }

        val error = assertFailsWith<HttpError> {
            service.consume(
                contentId = "contentId",
                userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
            )
        }

        assertEquals(
            expected = HttpCode.Gone,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Qr code token already consumed",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if qrtoken is expired`() = runTest {
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = {
            qrCodeTokenModelFixture.copy(
                status = QrCodeTokenStatusModel.GENERATED,
                expiresAt = clock.now() - 1.seconds
            )
        }

        val error = assertFailsWith<HttpError> {
            service.consume(
                contentId = "contentId",
                userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
            )
        }

        assertEquals(
            expected = HttpCode.Gone,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Qr code token is expired",
            actual = error.message
        )
    }

    @Test
    fun `should consume qrtoken if authorization succeeds`() = runTest {
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = {
            qrCodeTokenModelFixture.copy(
                status = QrCodeTokenStatusModel.GENERATED
            )
        }

        service.consume(
            contentId = "contentId",
            userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        assertEquals(
            expected = 1,
            actual = qrCodeTokenRepository.consumeCalls
        )
    }

    @Test
    fun `should store refresh token if authorization succeeds`() = runTest {
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = {
            qrCodeTokenModelFixture.copy(
                status = QrCodeTokenStatusModel.GENERATED
            )
        }
        authTokenManager.refreshToken = "refreshToken"

        service.consume(
            contentId = "contentId",
            userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        assertEquals(
            expected = 1,
            actual = authTokenManager.generateRefreshTokenCalls
        )

        assertEquals(
            actual = storeTokenService.callHistory.first(),
            expected = Pair(
                "refreshToken",
                UUID.fromString("00000000-0000-0000-0000-000000000001")
            )
        )
    }


    @Test
    fun `should store authorized qrtoken if authorization succeeds`() = runTest {
        val rawContent = "rawContent"
        qrCodeTokenRepository.getQrCodeTokenByIdResponse = {
            qrCodeTokenModelFixture.copy(
                status = QrCodeTokenStatusModel.GENERATED,
                rawContent = rawContent
            )
        }
        authTokenManager.refreshToken = "refreshToken"
        authTokenManager.token = "accessToken"

        service.consume(
            contentId = "contentId",
            userUuid = UUID.fromString("00000000-0000-0000-0000-000000000001")
        )

        assertEquals(
            expected = 1,
            actual = qrCodeTokenRepository.storeAuthorizedTokenCallstack.size
        )

        assertEquals(
            expected = Pair(
                "contentId", AuthorizedQrCodeToken(
                    rawContent = rawContent,
                    refreshToken = "refreshToken",
                    accessToken = "accessToken"
                )
            ),
            actual = qrCodeTokenRepository.storeAuthorizedTokenCallstack.first()
        )
    }
}
