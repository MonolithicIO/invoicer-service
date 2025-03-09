package services.impl.qrcodetoken

import foundation.authentication.impl.AuthTokenManager
import kotlinx.datetime.Instant
import models.login.AuthTokenModel
import models.qrcodetoken.QrCodeTokenStatusModel
import repository.api.repository.QrCodeTokenRepository
import services.api.services.login.StoreRefreshTokenService
import services.api.services.qrcodetoken.ConsumeQrCodeTokenService
import services.api.services.user.GetUserByIdService
import utils.date.impl.DateProvider
import utils.exceptions.goneError
import utils.exceptions.notFoundError

internal class ConsumeQrCodeTokenServiceImpl(
    private val authTokenManager: AuthTokenManager,
    private val storeRefreshTokenService: StoreRefreshTokenService,
    private val qrCodeTokenRepository: QrCodeTokenRepository,
    private val dateProvider: DateProvider,
    private val getUserByIdService: GetUserByIdService
) : ConsumeQrCodeTokenService {

    override suspend fun consume(
        contentId: String,
        userUuid: String
    ): AuthTokenModel {
        val token = qrCodeTokenRepository.getQrCodeByTokenId(
            contentId = contentId
        ) ?: notFoundError("Qr Code Token not found")

        if (token.status == QrCodeTokenStatusModel.CONSUMED) {
            goneError("Qr code token already consumed")
        }

        if (checkExpiredToken(token.expiresAt)) {
            goneError("Qr code token is expired")
        }

        val user = getUserByIdService.get(userUuid)

        val authorizationToken = authTokenManager.generateToken(user.id.toString())
        val refreshToken = authTokenManager.generateRefreshToken(user.id.toString())

        storeRefreshTokenService.storeRefreshToken(
            token = refreshToken,
            userId = user.id.toString()
        )

        qrCodeTokenRepository.consumeQrCodeToken(token.id)

        return AuthTokenModel(
            accessToken = authorizationToken,
            refreshToken = refreshToken
        )
    }

    private fun checkExpiredToken(expiresAt: Instant): Boolean {
        return dateProvider.currentInstant() > expiresAt
    }
}