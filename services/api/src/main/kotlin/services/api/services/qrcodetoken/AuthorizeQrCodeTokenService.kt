package services.api.services.qrcodetoken

import java.util.*

interface ConsumeQrCodeTokenService {
    suspend fun consume(
        contentId: String,
        userUuid: UUID,
    )
}