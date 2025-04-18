package services.api.services.qrcodetoken

import models.qrcodetoken.AuthorizedQrCodeToken
import kotlin.time.Duration

interface PollAuthorizedTokenService {
    suspend fun poll(
        contentId: String,
        interval: Duration,
    ): Response

    sealed interface Response {
        data class CloseConnection(val message: String) : Response
        data class Success(val token: AuthorizedQrCodeToken) : Response
    }
}