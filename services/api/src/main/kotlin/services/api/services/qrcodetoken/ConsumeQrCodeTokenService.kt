package services.api.services.qrcodetoken

interface ConsumeQrCodeTokenService {
    suspend fun consume(
        contentId: String,
        userUuid: String,
    )
}