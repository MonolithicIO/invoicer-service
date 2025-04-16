package services.impl.qrcodetoken

import repository.fakes.FakeQrCodeTokenRepository
import kotlin.test.BeforeTest

class GetQrCodeTokenByContentIdServiceImplTest {
    private lateinit var service: GetQrCodeTokenByContentIdServiceImpl
    private lateinit var repository: FakeQrCodeTokenRepository

    @BeforeTest
    fun setUp() {
        repository = FakeQrCodeTokenRepository()
        service = GetQrCodeTokenByContentIdServiceImpl(
            qrCodeTokenRepository = repository
        )
    }
}