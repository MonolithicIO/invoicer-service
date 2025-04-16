package services.impl.qrcodetoken

import foundation.cache.fakes.FakeCacheHandler
import io.github.alaksion.invoicer.foundation.log.fakes.FakeLogger
import kotlinx.coroutines.test.StandardTestDispatcher
import services.api.fakes.qrcode.FakeGetQrCodeTokenByContentIdService
import kotlin.test.BeforeTest

class PollAuthorizedTokenServiceImplTest {

    private lateinit var service: PollAuthorizedTokenServiceImpl
    private lateinit var cacheHandler: FakeCacheHandler
    private lateinit var getTokenService: FakeGetQrCodeTokenByContentIdService
    private lateinit var logger: FakeLogger

    @BeforeTest
    fun setUp() {
        cacheHandler = FakeCacheHandler()
        getTokenService = FakeGetQrCodeTokenByContentIdService()
        logger = FakeLogger()

        service = PollAuthorizedTokenServiceImpl(
            cacheHandler = cacheHandler,
            getTokenService = getTokenService,
            dispatcher = StandardTestDispatcher(),
            logger = logger
        )
    }
}