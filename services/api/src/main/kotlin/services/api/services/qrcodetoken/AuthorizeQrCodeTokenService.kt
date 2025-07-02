package services.api.services.qrcodetoken

import java.util.*

interface AuthorizeQrCodeTokenService {
    suspend fun consume(
        contentId: String,
        userUuid: UUID,
    )
}
