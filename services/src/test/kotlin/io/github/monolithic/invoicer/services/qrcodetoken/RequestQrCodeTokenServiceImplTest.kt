package io.github.monolithic.invoicer.services.qrcodetoken

import io.github.monolithic.invoicer.foundation.qrcode.fakes.FakeQrCodeGenerator
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.login.RequestLoginCodeModel
import io.github.monolithic.invoicer.repository.fakes.FakeQrCodeTokenRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError

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
