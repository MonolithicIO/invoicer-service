package services.impl.qrcodetoken

import kotlinx.coroutines.test.runTest
import repository.fakes.FakeQrCodeTokenRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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

    @Test
    fun `should call repository with correct contentId`() = runTest {
        val contentId = "testContentId"
        service.find(contentId)

        assertEquals(
            expected = "testContentId",
            actual = repository.getByContentIdCallstack.first()
        )

        assertEquals(
            expected = 1,
            actual = repository.getByContentIdCallstack.size
        )
    }
}
