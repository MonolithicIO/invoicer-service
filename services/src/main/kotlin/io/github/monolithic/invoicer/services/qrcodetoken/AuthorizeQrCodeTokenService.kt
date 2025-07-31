package io.github.monolithic.invoicer.services.qrcodetoken

import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.services.login.StoreRefreshTokenService
import java.util.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import io.github.monolithic.invoicer.models.qrcodetoken.AuthorizedQrCodeToken
import io.github.monolithic.invoicer.models.qrcodetoken.QrCodeTokenStatusModel
import io.github.monolithic.invoicer.repository.QrCodeTokenRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.goneError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface AuthorizeQrCodeTokenService {
    suspend fun consume(
        contentId: String,
        userUuid: UUID,
    )
}

internal class AuthorizeQrCodeTokenServiceImpl(
    private val authTokenManager: AuthTokenManager,
    private val storeRefreshTokenService: StoreRefreshTokenService,
    private val qrCodeTokenRepository: QrCodeTokenRepository,
    private val clock: Clock,
    private val getUserByIdService: GetUserByIdService,
) : AuthorizeQrCodeTokenService {

    override suspend fun consume(
        contentId: String,
        userUuid: UUID
    ) {
        val token = qrCodeTokenRepository.getQrCodeByContentId(
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
        val refreshToken = authTokenManager.generateRefreshToken()

        storeRefreshTokenService.createRefreshToken(
            token = refreshToken,
            userId = userUuid
        )

        qrCodeTokenRepository.storeAuthorizedToken(
            contentId = contentId,
            token = AuthorizedQrCodeToken(
                rawContent = token.rawContent,
                refreshToken = refreshToken,
                accessToken = accessToken
            )
        )
    }

    private fun checkExpiredToken(expiresAt: Instant): Boolean {
        return clock.now() > expiresAt
    }
}
