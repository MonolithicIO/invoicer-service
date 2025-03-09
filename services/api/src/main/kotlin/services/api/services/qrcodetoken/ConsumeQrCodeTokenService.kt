package services.api.services.qrcodetoken

import models.login.AuthTokenModel

interface ConsumeQrCodeTokenService {
    suspend fun consume(
        contentId: String,
        userUuid: String,
    ): AuthTokenModel
}