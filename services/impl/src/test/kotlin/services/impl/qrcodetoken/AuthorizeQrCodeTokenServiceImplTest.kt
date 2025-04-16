package services.impl.qrcodetoken

import foundation.authentication.fakes.FakeAuthTokenManager
import foundation.cache.fakes.FakeCacheHandler
import io.github.alaksion.invoicer.utils.fakes.FakeClock
import repository.fakes.FakeQrCodeTokenRepository
import services.api.fakes.refreshtoken.FakeStoreRefreshTokenService
import services.api.fakes.user.FakeGetUserByIdService
import kotlin.test.BeforeTest

class AuthorizeQrCodeTokenServiceImplTest {
    private lateinit var service: AuthorizeQrCodeTokenServiceImpl
    private lateinit var storeTokenService: FakeStoreRefreshTokenService
    private lateinit var clock: FakeClock
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var cacheHandler: FakeCacheHandler
    private lateinit var authTokenManager: FakeAuthTokenManager
    private lateinit var qrCodeTokenRepository: FakeQrCodeTokenRepository


    @BeforeTest
    fun setUp() {
        storeTokenService = FakeStoreRefreshTokenService()
        clock = FakeClock()
        getUserByIdService = FakeGetUserByIdService()
        cacheHandler = FakeCacheHandler()
        authTokenManager = FakeAuthTokenManager()
        qrCodeTokenRepository = FakeQrCodeTokenRepository()

        service = AuthorizeQrCodeTokenServiceImpl(
            authTokenManager = authTokenManager,
            storeRefreshTokenService = storeTokenService,
            qrCodeTokenRepository = qrCodeTokenRepository,
            clock = clock,
            getUserByIdService = getUserByIdService,
            cacheHandler = cacheHandler
        )
    }
}