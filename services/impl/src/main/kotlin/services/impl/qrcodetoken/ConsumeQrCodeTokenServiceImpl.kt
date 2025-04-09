package services.impl.qrcodetoken

import foundation.authentication.impl.AuthTokenManager
import io.github.alaksion.invoicer.utils.events.QrCodeEventHandler
import io.github.alaksion.invoicer.utils.events.QrCodeLoginEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import models.qrcodetoken.QrCodeTokenStatusModel
import repository.api.repository.QrCodeTokenRepository
import services.api.services.login.StoreRefreshTokenService
import services.api.services.qrcodetoken.ConsumeQrCodeTokenService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.goneError
import utils.exceptions.http.notFoundError
import java.util.*

internal class ConsumeQrCodeTokenServiceImpl(
    private val authTokenManager: AuthTokenManager,
    private val storeRefreshTokenService: StoreRefreshTokenService,
    private val qrCodeTokenStream: QrCodeEventHandler,
    private val qrCodeTokenRepository: QrCodeTokenRepository,
    private val clock: Clock,
    private val getUserByIdService: GetUserByIdService
) : ConsumeQrCodeTokenService {

    override suspend fun consume(
        contentId: String,
        userUuid: UUID
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

        val accessToken = authTokenManager.generateToken(userUuid.toString())
        val refreshToken = authTokenManager.generateRefreshToken(userUuid.toString())
        storeRefreshTokenService.storeRefreshToken(
            token = refreshToken,
            userId = userUuid
        )

        qrCodeTokenStream.publishEvent(
            event = QrCodeLoginEvent(
                contentId = token.rawContent,
                refreshToken = refreshToken,
                accessToken = accessToken
            )
        )
    }

    private fun checkExpiredToken(expiresAt: Instant): Boolean {
        return clock.now() > expiresAt
    }
}