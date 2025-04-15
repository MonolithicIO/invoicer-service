package controller.viewmodel.qrcodetoken

import kotlinx.datetime.Instant
import models.qrcodetoken.QrCodeTokenModel
import models.qrcodetoken.QrCodeTokenStatusModel
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class QrCodeTokenDetailsViewModelTest {

    @Test
    fun `converts QrCodeTokenModel to QrCodeTokenDetailsViewModel`() {
        val qrCodeTokenModel = QrCodeTokenModel(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            agent = "Test Agent",
            base64Content = "dGVzdA==",
            rawContent = "test",
            ipAddress = "192.168.1.1",
            status = QrCodeTokenStatusModel.GENERATED,
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            expiresAt = Instant.parse("2023-01-03T00:00:00Z")
        )

        val result = qrCodeTokenModel.toTokenDetailsViewModel()

        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.id)
        assertEquals("Test Agent", result.agent)
        assertEquals("dGVzdA==", result.base64Content)
        assertEquals("test", result.rawContent)
        assertEquals("192.168.1.1", result.ipAddress)
        assertEquals(QrCodeTokenStatusViewModel.GENERATED, result.status)
        assertEquals(Instant.parse("2023-01-01T00:00:00Z"), result.createdAt)
        assertEquals(Instant.parse("2023-01-02T00:00:00Z"), result.updatedAt)
        assertEquals(Instant.parse("2023-01-03T00:00:00Z"), result.expiresAt)
    }
}