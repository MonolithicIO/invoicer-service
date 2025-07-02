package services.impl.qrcodetoken

import foundation.qrcode.fakes.FakeQrCodeGenerator
import kotlinx.coroutines.test.runTest
import models.login.RequestLoginCodeModel
import repository.fakes.FakeQrCodeTokenRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

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

    @Test
    fun `should throw error if size is less than 1`() = runTest {
        val error = assertFailsWith<HttpError> {
            service.requestQrCodeToken(
                request = RequestLoginCodeModel(
                    ipAddress = "",
                    agent = "",
                    size = 0
                )
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Size must be greater than 0",
            actual = error.message
        )
    }

    @Test
    fun `should call repository on successful code generation`() = runTest {
        service.requestQrCodeToken(
            request = RequestLoginCodeModel(
                ipAddress = "",
                agent = "",
                size = 10
            )
        )

        assertEquals(
            expected = 1,
            actual = qrCodeTokenRepository.createCalls
        )
    }
}
