package services.impl.qrcodetoken

import io.github.alaksion.invoicer.foundation.log.fakes.FakeLogger
import kotlinx.coroutines.test.StandardTestDispatcher
import repository.fakes.FakeQrCodeTokenRepository
import services.api.fakes.qrcode.FakeGetQrCodeTokenByContentIdService
import kotlin.test.BeforeTest

class PollAuthorizedTokenServiceImplTest {

    private lateinit var service: PollAuthorizedTokenServiceImpl
    private lateinit var qrCodeTokenRepository: FakeQrCodeTokenRepository
    private lateinit var getTokenService: FakeGetQrCodeTokenByContentIdService
    private lateinit var logger: FakeLogger

    @BeforeTest
    fun setUp() {
        qrCodeTokenRepository = FakeQrCodeTokenRepository()
        getTokenService = FakeGetQrCodeTokenByContentIdService()
        logger = FakeLogger()

        service = PollAuthorizedTokenServiceImpl(
            qrCodeTokenRepository = qrCodeTokenRepository,
            getTokenService = getTokenService,
            dispatcher = StandardTestDispatcher(),
            logger = logger
        )
    }
}