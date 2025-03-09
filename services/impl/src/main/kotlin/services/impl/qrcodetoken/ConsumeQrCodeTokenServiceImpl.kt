package services.impl.qrcodetoken

import kotlinx.datetime.Instant
import models.qrcodetoken.QrCodeTokenStatusModel
import repository.api.repository.QrCodeTokenRepository
import services.api.services.qrcodetoken.ConsumeQrCodeTokenService
import services.api.services.user.GetUserByIdService
import utils.date.impl.DateProvider
import utils.exceptions.goneError
import utils.exceptions.notFoundError

internal class ConsumeQrCodeTokenServiceImpl(
    private val qrCodeTokenRepository: QrCodeTokenRepository,
    private val dateProvider: DateProvider,
    private val getUserByIdService: GetUserByIdService
) : ConsumeQrCodeTokenService {

    override suspend fun consume(
        contentId: String,
        userUuid: String
    ) {
        val token = qrCodeTokenRepository.getQrCodeByTokenId(
            contentId = contentId
        ) ?: notFoundError("Qr Code Token not found")

        if (token.status == QrCodeTokenStatusModel.CONSUMED) {
            goneError("Qr code token already consumed")
        }

        if (checkExpiredToken(token.expiresAt)) {
            goneError("Qr code token is expired")
        }

        getUserByIdService.get(userUuid)

        qrCodeTokenRepository.consumeQrCodeToken(token.id)
        // TODO -> Once QrCode is consumed what to do next?
    }

    private fun checkExpiredToken(expiresAt: Instant): Boolean {
        return dateProvider.currentInstant() > expiresAt
    }
}