package services.impl.qrcodetoken

import foundation.cache.CacheHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.selects.select
import models.qrcodetoken.AuthorizedQrCodeToken
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.PollAuthorizedTokenService
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal class PollAuthorizedTokenServiceImpl(
    private val cacheHandler: CacheHandler,
    private val getTokenService: GetQrCodeTokenByContentIdService,
    private val dispatcher: CoroutineDispatcher
) : PollAuthorizedTokenService {

    override suspend fun poll(
        contentId: String,
        interval: Duration,
    ): PollAuthorizedTokenService.Response {
        return coroutineScope {
            getTokenService.find(contentId)
                ?: return@coroutineScope PollAuthorizedTokenService.Response.CloseConnection("Token not found")

            val cancellation = async(dispatcher) {
                delay(60.seconds)
                PollAuthorizedTokenService.Response.CloseConnection("Connection timed out")
            }

            val poll = async(dispatcher) { pollToken(contentId) }

            select {
                cancellation.onAwait { it }
                poll.onAwait { it }
            }.also {
                if (cancellation.isActive) cancellation.cancel()
                if (poll.isActive) poll.cancel()
            }
        }
    }

    private suspend fun pollToken(
        contentId: String
    ): PollAuthorizedTokenService.Response {
        val authData = cacheHandler.get(key = contentId, serializer = AuthorizedQrCodeToken.serializer())
        if (authData != null) {
            cacheHandler.delete(contentId)
            return PollAuthorizedTokenService.Response.Success(authData)
        } else {
            delay(60.seconds)
            return pollToken(contentId)
        }
    }
}