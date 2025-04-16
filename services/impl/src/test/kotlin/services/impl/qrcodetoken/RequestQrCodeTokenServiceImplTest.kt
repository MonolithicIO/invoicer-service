package services.impl.qrcodetoken

import foundation.qrcode.fakes.FakeQrCodeGenerator
import repository.fakes.FakeQrCodeTokenRepository
import kotlin.test.BeforeTest

class RequestQrCodeTokenServiceImplTest {

    private lateinit var service: RequestQrCodeTokenServiceImpl
    private lateinit var qrCodeGenerator: FakeQrCodeGenerator
    private lateinit var qrCodeTokenRepository: FakeQrCodeTokenRepository

    @BeforeTest
    fun setUp() {
        qrCodeGenerator = FakeQrCodeGenerator()
        qrCodeTokenRepository = FakeQrCodeTokenRepository()

        service = RequestQrCodeTokenServiceImpl(
            qrCodeGenerator = qrCodeGenerator,
            qrCodeTokenRepository = qrCodeTokenRepository
        )
    }
}