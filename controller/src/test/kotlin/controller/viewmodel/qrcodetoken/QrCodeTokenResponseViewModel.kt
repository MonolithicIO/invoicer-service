package controller.viewmodel.qrcodetoken

import kotlinx.datetime.Instant
import models.qrcodetoken.QrCodeTokenModel
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class QrCodeTokenResponseViewModelTest {

    @Test
    fun `converts QrCodeTokenModel to QrCodeTokenResponseViewModel`() {
        val qrCodeTokenModel = QrCodeTokenModel(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            agent = "Test Agent",
            base64Content = "dGVzdA==",
            rawContent = "test",
            ipAddress = "192.168.1.1",
            status = models.qrcodetoken.QrCodeTokenStatusModel.GENERATED,
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            expiresAt = Instant.parse("2023-01-03T00:00:00Z")
        )

        val result = qrCodeTokenModel.toTokenResponseViewModel()

        assertEquals("dGVzdA==", result.base64Content)
        assertEquals(Instant.parse("2023-01-03T00:00:00Z"), result.expiration)
        assertEquals("test", result.rawContent)
    }
}